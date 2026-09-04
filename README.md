![Contextium app icon](icon.png)

# Contextium
    
Contextium extends Android's native text-selection context menu with configurable actions for web searches. Select text in almost any app (e.g. your browser), choose **Contextium** from the context menu and open the selected text with one of your custom search URLs.

Since I search for audiobooks on a daily basis across a variety of websites, I realized that the repeated search steps could be streamlined. Contextium was created to shorten these repetitive workflows by turning them into one‑tap actions directly from Android’s text‑selection menu.

[<img src="https://f-droid.org/badge/get-it-on.png"
    alt="Get it on F-Droid"
    height="80">](https://f-droid.org/packages/app.xcy7e.contextium)

## Features

- Create unlimited context‑menu actions
- Configure name, label, target URL, and search parameter
- Reorder actions via drag-and-drop
- Enable or disable actions individually
- Access all actions from Android’s native text‑selection menu
- Export and import your configuration for backup

## Privacy
Contextium performs simple URL‑based searches and does **not** collect or transmit personal data. All configuration data is stored locally on your device.

## Installation

### App Store

#### F-Droid (recommended)
1. Open **F-Droid App** and search for `Contextium`
2. Or visit the [F-Droid Website](https://f-droid.org/packages/app.xcy7e.contextium) 

### Manual installation

Get the latest APK from the [Releases page](https://github.com/xcy7e/Contextium/releases/latest).

1. Download the APK
2. Open it in your file manager to install
3. If prompted, allow your file manager to install unknown apps


## How It Works
The workflow looks like this:

|                                   1. Add menu entries                                   |                              2. Configure each entry                              |
|:---------------------------------------------------------------------------------------:|:---------------------------------------------------------------------------------:|
|              ![Main app screen](assets/img/screenshots/1-MainActivity.png)              | ![Context menu entry settings](assets/img/screenshots/2-EditContextMenuEntry.png) |
|                      **3. Select text, then choose `Contextium`**                       |                  **4. Choose an action to start the web search**                  |
| ![Native Android context menu](assets/img/screenshots/3-ContextiumContextMenuEntry.png) |   ![Contextium item picker](assets/img/screenshots/4-ContextiumContextMenu.png)   |

### Requirements

The target website must support passing search terms via URL parameters.

### URL and Parameter

Enter the target URL and the parameter name used by the website. Contextium builds the final URL when text is selected.

You can determine the correct search parameter by performing a search on the website and inspecting the resulting URL.
For example, if you search for `Computer Mouse`, the URL might look like this:

`https://www.example.com/page/?searched=Computer%20Mouse`

In this case, the search parameter is `searched`. It must appear after `?` or `&`, followed by `=`, and then your search term.
If the URL does not contain your search term, the website is probably using a POST request instead of a GET request. Since POST requests do not expose search terms in the URL, Contextium cannot be used with such websites.

### Examples

```yaml
# Example 1: URL without existing parameters
URL: https://www.google.com/search
PARAM: q

# Selected text: "foobar"
# Request URL: https://www.google.com/search?q=foobar
```

```yaml
# Example 2: URL with other existing parameters
URL: https://www.tradingview.com/chart/?x=y
PARAM: symbol

# Selected text: "FOO BAR"
# Request URL: https://www.tradingview.com/chart/?x=y&symbol=FOO%20BAR
```

## Usage suggestions
Here are some ideas for what Contextium is useful for. Anything you regularly search on the same page is *perfectly suited* for Contextium.
You could search for..
- **Books** (Google Books, Goodreads, …)
- **Audiobooks** (Audible, BookBeat, …)
- **Products** (Amazon, Walmart, Alibaba, …)
- **Stocks** (Yahoo Finance, Google Finance, MarketWatch, …)
- **Movies** (IMDb, Letterboxd, Rotten Tomatoes, …)
- **TV shows** (TheTVDB, JustWatch, …)
- **Music** (Spotify, Apple Music, Discogs, …)
- **Games** (Steam, Metacritic, IGDB, …)
- **Recipes**, **Translations**, **Academic topics**, **Patents**, **Jobs** and more..

---

## Weblinks

- [Repository on Github](https://github.com/xcy7e/Contextium)
- The privacy policy is available [here](https://contextium.xcy7e.app/privacy) and in `PRIVACY.md` in this repository
