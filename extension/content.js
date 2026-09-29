(function () {
  const here = SayyadVerdict(location.href);
  // The model only knows web addresses, so file:// and chrome:// pages never get the banner.
  if (here.risky && location.protocol.startsWith("http") && document.body) {
    const bar = document.createElement("div");
    bar.id = "Sayyad-banner";
    const why = SayyadReasons(location.href, location.hostname).join(". ");
    bar.innerHTML = `<b>Sayyad:</b> this page looks like phishing (${Math.round(here.p * 100)}%). ${why}. ` +
                    `<button id="Sayyad-close">I understand</button>`;
    document.body.prepend(bar);
    bar.querySelector("#Sayyad-close").onclick = () => bar.remove();
  }

  const links = Array.from(document.querySelectorAll("a[href^='http']")).slice(0, 300);
  let flagged = 0;
  for (const a of links) {
    const v = SayyadVerdict(a.href);
    if (v.risky) {
      a.classList.add("Sayyad-risky");
      a.title = `Sayyad: ${Math.round(v.p * 100)}% likely phishing. ` + SayyadReasons(a.href, SayyadHost(a.href)).join(". ");
      flagged++;
    }
  }
  if (chrome.storage && chrome.storage.local) {
    chrome.storage.local.get({ scanned: 0, flagged: 0 }, s =>
      chrome.storage.local.set({ scanned: s.scanned + links.length, flagged: s.flagged + flagged }));
  }
})();
