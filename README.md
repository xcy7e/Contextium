<div align="center">

<img src="./assets/img/icon-128px.png" width="96" alt="Logo" style="margin-bottom:0;max-width: 100%;" />

<h1 style="margin-top:0;max-width: 100%;">Contextium</h1>

[![F-Droid version](https://img.shields.io/f-droid/v/app.xcy7e.contextium?label=F-Droid&logo=f-roid&color=%23b2eb0c)](https://gitlab.com/fdroid/fdroiddata/-/blob/master/metadata/app.xcy7e.contextium.yml)
[![GitHub version](https://img.shields.io/github/v/release/xcy7e/Contextium?label=Github&logo=github&color=%23ffffff)](https://github.com/xcy7e/Contextium/releases)
[![VirusTotal](https://img.shields.io/badge/dynamic/json?label=VirusTotal&query=%24.message&url=https%3A%2F%2Fraw.githubusercontent.com%2Fxcy7e%2FContextium%2Fvt-badge%2Fvt-badge.json&color=green&logo=virustotal&logoColor=white)](https://xcy7e.github.io/Contextium/vt-latest.html)

Contextium extends Android's native text-selection menu with configurable actions for web searches.
Select text in almost any app (e.g., your browser), choose **Contextium** from the context menu,
and open the selected text using one of your custom search URLs.

Since I search for audiobooks daily across various websites, I realized these repeated search steps
could be streamlined. Contextium was built to simplify these repetitive workflows by turning them
into one‑tap actions directly from Android’s text‑selection menu.

[<img src="https://f-droid.org/badge/get-it-on.png" alt="Get it on F-Droid" height="80">](https://f-droid.org/packages/app.xcy7e.contextium) [<img src="https://raw.githubusercontent.com/rubenpgrady/get-it-on-github/refs/heads/main/get-it-on-github.png" alt="Get it on Github" height="80">](https://github.com/xcy7e/Contextium/releases/latest)

</div>

---

1. [Features](#Features)
2. [Installation](#Installation)
3. [How it works](#How-it-works)
    - [Requirements](#Requirements)
    - [How to find out the required URL for the entry?](#How-to-find-out-the-required-URL-for-the-entry)
4. [Examples](#Examples)
    - [3 different URL cases](#3-different-URL-cases)
    - [Example file](#Example-file)
5. [Usage suggestions](#Usage-suggestions)

## Features

- Create unlimited context‑menu actions
- Configure individual search URLs
- Access all actions from Android’s native text‑selection menu
- Export and import your configuration for backup
- Reorder actions via drag-and-drop
- Enable or disable actions individually

## Installation

### F-Droid (recommended)

1. Open **F-Droid App** and search for `Contextium`
2. Or visit the [F-Droid Website](https://f-droid.org/packages/app.xcy7e.contextium)

### Github

Get the latest APK from the [Releases page](https://github.com/xcy7e/Contextium/releases/latest).

1. Download the APK
2. Open it in your file manager to install
3. If prompted, allow your file manager to install unknown apps

## How it works

The workflow looks like this:

|                                                                              1. Add menu entries                                                                              |                                            2. Configure each entry ([How?](#How-to-find-out-the-required-URL-for-the-entry))                                            |
|:-----------------------------------------------------------------------------------------------------------------------------------------------------------------------------:|:-----------------------------------------------------------------------------------------------------------------------------------------------------------------------:|
|              <img src="https://raw.githubusercontent.com/xcy7e/Contextium/master/assets/img/screenshots/1-MainActivity.png" width="280" alt="Main app screen" />              | <img src="https://raw.githubusercontent.com/xcy7e/Contextium/master/assets/img/screenshots/2-EditContextMenuEntry.png" width="280" alt="Context menu entry settings" /> |
|                                                                 **3. Select text, then choose `Contextium`**                                                                  |                                                             **4. Choose an action to start the web search**                                                             |
| <img src="https://raw.githubusercontent.com/xcy7e/Contextium/master/assets/img/screenshots/3-ContextiumContextMenuEntry.png" width="280" alt="Native Android context menu" /> |   <img src="https://raw.githubusercontent.com/xcy7e/Contextium/master/assets/img/screenshots/4-ContextiumContextMenu.png" width="280" alt="Contextium item picker" />   |

### Requirements

The target website **must** support passing search terms via URL parameter.

I recommend importing the [example backup](#Example-file) for a quick start.

### How to find out the required URL for the entry?

1. Go to the website you want to link in Contextium
2. Execute the search with any search term and potential filters
3. View the full URL after submitting the search

    - Check if your search term appears in the URL: If it does, great! If not, this search might
      not work with Contextium.

        - For example, if you searched for `Computer Mouse`, the URL might look like this:\
          `https://www.example.com/page/?search=Computer%20Mouse`

    - Now, replace your search term with `%s`:\
      `https://www.example.com/page/?search=%s`
    - Then remove any unnecessary parameters and their values (such as hashes, etc.)

4. Enter the modified URL in a Contextium item and give it a try by selecting text in your browser
   and choose 'Contextium' from the context menu.

## Examples

### 3 different URL cases

```yaml
# Example 1: URL without existing parameters
URL: https://www.google.com/search
PARAM: q

# Selected text: "foobar"
# Item URL: https://www.google.com/search?q=%s
# Result: https://www.google.com/search?q=foobar
```

```yaml
# Example 2: URL with additional parameters
URL: https://www.tradingview.com/chart/?x=y
PARAM: symbol

# Selected text: "FOO BAR"
# Item URL: https://www.tradingview.com/chart/?x=y&symbol=%s
# Result: https://www.tradingview.com/chart/?x=y&symbol=FOO%20BAR
```

```yaml
# Example 3: URL without a named parameter
URL: https://www.example.com/%s
PARAM: (leave empty)

# Selected text: "FOO BAR"
# Item URL: https://www.example.com/%s
# Result: https://www.example.com/FOO%20BAR
```

### Example file

You can download and import
the [example.backup.json](https://github.com/xcy7e/Contextium/blob/master/examples/example.backup.json)
file,
to get a glimpse of what's possible with **Contextium**.
Simply open the Menu in Contextium (swipe right), tap *Import* and select the `json`-file.
If you already have items, you might want to back up them first.

The Example file contains numerous working example entries:

- Ask AI (ChatGPT, Gemini, Claude, …)
- Search web (Wikipedia, Google, DuckDuckGo, Internet Archive, …)
- Search media (Google Images, OpenLibrary, iMDB, Audible, Goodreads, …)
- Search products (Amazon, eBay, …)
- Search forums (Reddit, StackOverflow, …)
- Developer tools (github, npm, packagist, urlscan.io, …)
- ...

This file is meant to help you understanding the different types of URLs, some are short, some are
longer with additional parameter, and some without a named parameter.
You can of course remove unwanted entries and customize it as you like.

## Usage suggestions

Here you'll find some inspiration for how Contextium can be used.
This is just a selection – **you can add any website** that supports URL-based searches.

Anything you regularly search for is *perfectly suited* for Contextium.

You could, for example, search for...

- **AI** (ChatGPT, Gemini, Claude, Grok, Perplexity, …)
- **Search engines** (Google, DuckDuckGo, Bing, Metacrawler, …)
- **Books** (Google Books, Goodreads, …)
- **Audiobooks** (Audible, BookBeat, …)
- **Products** (Amazon, eBay, Walmart, Alibaba, …)
- **Stocks** (Yahoo Finance, Google Finance, MarketWatch, …)
- **Movies** (Youtube, Rumble, IMDb, Letterboxd, Rotten Tomatoes, …)
- **TV shows** (TheTVDB, JustWatch, …)
- **Music** (Spotify, Apple Music, Discogs, …)
- **Games** (Steam, Metacritic, IGDB, …)
- **Developer tools** (Whois, encryption, scanner, …)
- **Recipes**, **Translators**, **Academic topics**, **Patents**, **Jobs** and more...

---

## Weblinks

- [Repository on Github](https://github.com/xcy7e/Contextium)
- The privacy policy is available [here](https://contextium.xcy7e.app/privacy) and in the
  `PRIVACY.md` file in this repository.

## Privacy

Contextium performs simple URL‑based search requests through your default browser app and therefor
does **not** collect or transmit personal data.

All configuration data is stored locally on your device.