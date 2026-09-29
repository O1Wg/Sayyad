function render(url) {
  const v = SayyadVerdict(url);
  document.getElementById("url").textContent = url;
  document.getElementById("score").textContent = Math.round(v.p * 100) + "%";
  const verdict = document.getElementById("verdict");
  verdict.textContent = v.risky ? "Looks like phishing" : (v.trusted ? "Known site" : "Looks fine");
  verdict.className = v.risky ? "bad" : "good";
  const ul = document.getElementById("reasons");
  ul.innerHTML = "";
  for (const r of SayyadReasons(url, SayyadHost(url))) {
    const li = document.createElement("li"); li.textContent = r; ul.appendChild(li);
  }
}
chrome.tabs.query({ active: true, currentWindow: true }, tabs => {
  const t = tabs[0];
  if (t && t.url && t.url.startsWith("http")) render(t.url);
});
document.getElementById("check").onclick = () => {
  const u = document.getElementById("paste").value.trim();
  if (u) render(u);
};
chrome.storage.local.get({ scanned: 0, flagged: 0 }, s => {
  document.getElementById("stats").textContent = `${s.scanned} links scanned on your pages, ${s.flagged} flagged`;
});
