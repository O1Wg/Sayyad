# Sayyad (صيّاد)
 
A Chrome extension that warns you about phishing links before you click. Everything runs in your browser; nothing is sent to a server.
 
*Sayyad* means "the fisherman" in Arabic: phishers fish for passwords, and Sayyad catches their links.

Here's a video demnstration of the project:
[![Sayyad Project (مشروع صيّاد)](https://img.youtube.com/vi/JbLUng9pQCk/hqdefault.jpg)](https://www.youtube.com/watch?v=JbLUng9pQCk)
 
## What it does
 
- Outlines risky links in red, and tells you why when you hover over them.
- Shows a red banner if the page you are on looks like phishing.
- Catches fake Saudi brands, like a link that mentions Absher but isn't on absher.sa.
- Never flags well-known sites, or any `.gov.sa` or `.edu.sa` site.
## How it works
 
A logistic regression model, trained in Java with Weka on 549,346 labelled web addresses, reads 16 clues from the address alone: its length, an IP address, `@`, scam words, endings like `.xyz`, link shorteners and more. The whole model is 17 numbers, so it fits inside the extension.
 
```
warn = (score ≥ 90%  OR  fake Saudi brand)  AND NOT  on the trusted list
```
 
## Results
 
Tested on 45,718 links the model never saw:
 
| Precision | False alarms | Recall |
|---|---|---|
| 98.3% | 0.5% | 26% |
 
98.3% of warnings were right, and only 0.5% of real links were flagged. The trade-off is that the model alone catches about a quarter of phishing links. On the test page (`tests/links.html`), all 11 fake links were caught and none of the 11 real sites were flagged.
 
## Install
 
1. Download this repo (**Code → Download ZIP**) and unzip it.
2. Open `chrome://extensions` and turn on **Developer mode**.
3. Click **Load unpacked** and choose the `extension` folder.
To retrain the model, put `weka.jar` and the Kaggle CSV in `trainer/`, then run `BuildArff`, `Train` and `Export`.
 
## Limitations
 
Sayyad reads only the address, not the page, and it was trained on an older public dataset.
