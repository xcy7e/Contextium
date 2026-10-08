import hashlib
import json
import os
import time
from pathlib import Path

import requests

API_KEY = os.environ["VT_API_KEY"]
APK_PATH = Path(os.environ["APK_PATH"])
OUTPUT_JSON = "vt-badge.json"
COMMENT_TEXT = os.environ.get(
    "VT_COMMENT",
    "Automated release scan"
)

BASE_URL = "https://www.virustotal.com/api/v3"
HEADERS = {
    "x-apikey": API_KEY,
}

POLL_INTERVAL = 5
POLL_TIMEOUT = 300

def sha256_file(path: Path) -> str:
    digest = hashlib.sha256()

    with path.open("rb") as file:
        for chunk in iter(lambda: file.read(1024 * 1024), b""):
            digest.update(chunk)

    return digest.hexdigest()


def get_file_report(file_hash: str):
    response = requests.get(
        f"{BASE_URL}/files/{file_hash}",
        headers=HEADERS,
        timeout=30,
    )

    if response.status_code == 404:
        return None

    response.raise_for_status()
    return response.json()


def upload_file(path: Path) -> str:
    with path.open("rb") as file:
        response = requests.post(
            f"{BASE_URL}/files",
            headers=HEADERS,
            files={"file": (path.name, file)},
            timeout=120,
        )

    if response.status_code == 409:
        print("VirusTotal throws 409 Conflict; use the existing report.")
        return ""

    response.raise_for_status()
    return response.json()["data"]["id"]


def wait_for_analysis(analysis_id: str) -> None:
    deadline = time.time() + POLL_TIMEOUT

    while time.time() < deadline:
        response = requests.get(
            f"{BASE_URL}/analyses/{analysis_id}",
            headers=HEADERS,
            timeout=30,
        )
        response.raise_for_status()

        attributes = response.json()["data"]["attributes"]
        status = attributes["status"]

        print(f"Analysis status: {status}")

        if status == "completed":
            return

        if status == "failed":
            raise RuntimeError("VirusTotal analysis failed.")

        time.sleep(POLL_INTERVAL)

    raise TimeoutError("Timeout while waiting for the VirusTotal analysis result.")


def add_comment(file_hash: str) -> None:
    payload = {
        "data": {
            "type": "comment",
            "attributes": {
                "text": COMMENT_TEXT,
            },
        }
    }

    response = requests.post(
        f"{BASE_URL}/files/{file_hash}/comments",
        headers=HEADERS,
        json=payload,
        timeout=30,
    )

    # 409 = Avoid duplicating comments
    if response.status_code == 409:
        print("Comment already exists; skipped.")
        return

    response.raise_for_status()


if not APK_PATH.is_file():
    raise FileNotFoundError(f"APK not found: {APK_PATH}")

file_hash = sha256_file(APK_PATH)
print(f"APK: {APK_PATH}")
print(f"SHA-256: {file_hash}")

report = get_file_report(file_hash)

if report is None:
    print("File not yet known to VirusTotal; Upload started.")
    analysis_id = upload_file(APK_PATH)

    if analysis_id:
        wait_for_analysis(analysis_id)

    report = get_file_report(file_hash)

    if report is None:
        raise RuntimeError(
            "No file report found after analysis."
        )
else:
    print("File is already known to VirusTotal.")

stats = report["data"]["attributes"]["last_analysis_stats"]

report_url = f"https://www.virustotal.com/gui/file/{file_hash}"

badge_data = {
    "schemaVersion": 1,
    "label": "VirusTotal",
    "message": f'{stats.get("malicious", 0)}/{sum(stats.values())} detected',
    "malicious": stats.get("malicious", 0),
    "harmless": stats.get("harmless", 0),
    "total": sum(stats.values()),
    "url": report_url,
}

add_comment(file_hash)

with open(OUTPUT_JSON, "w", encoding="utf-8") as f:
    json.dump(badge_data, f, indent=2)

print(f"Badge data written: {OUTPUT_JSON}")
print(json.dumps(badge_data, indent=2))
print(f"Report: {report_url}")