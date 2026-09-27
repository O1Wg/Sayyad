const Sayyad_NAMES = ["url_len", "host_len", "n_dots", "n_hyphens", "n_digits", "n_subdomains", "is_ip", "has_at",
  "dbl_slash", "n_special", "is_https", "n_susp", "bad_tld", "shortener", "path_len", "host_entropy", "digit_ratio_host"];
const Sayyad_SUSP = ["login", "signin", "sign-in", "verify", "verification", "secure", "security", "account", "update",
  "confirm", "password", "bank", "free", "bonus", "gift", "win", "prize", "alert", "urgent", "suspend", "unlock",
  "wallet", "invoice", "payment", "pay", "otp", "support"];
const Sayyad_BAD_TLD = new Set(["xyz", "top", "tk", "ml", "ga", "cf", "gq", "icu", "buzz", "click", "work", "link",
  "zip", "mov", "cam", "rest", "monster", "quest", "fit", "lol"]);
const Sayyad_SHORT = new Set(["bit.ly", "tinyurl.com", "t.co", "goo.gl", "cutt.ly", "is.gd", "ow.ly", "rebrand.ly",
  "shorturl.at", "tiny.cc", "rb.gy"]);

function SayyadFeatures(raw) {
  const url = String(raw).trim().toLowerCase();
  let https = 0, rest = url;
  if (url.startsWith("https://")) { https = 1; rest = url.slice(8); }
  else if (url.startsWith("http://")) { rest = url.slice(7); }
  let cut = rest.length;
  for (const c of ["/", "?", "#"]) { const i = rest.indexOf(c); if (i >= 0 && i < cut) cut = i; }
  let host = rest.slice(0, cut);
  const pathq = rest.slice(cut);
  const hasAt = url.includes("@") ? 1 : 0;
  const at = host.lastIndexOf("@"); if (at >= 0) host = host.slice(at + 1);
  const colon = host.indexOf(":"); if (colon >= 0) host = host.slice(0, colon);
  host = host.replace(/\.+$/, "");
  const labels = host ? host.split(".") : [];
  const tld = labels.length ? labels[labels.length - 1] : "";
  const count = (s, ch) => s.split(ch).length - 1;
  const digits = (s) => (s.match(/[0-9]/g) || []).length;
  const countAny = (s, set) => [...s].filter(x => set.includes(x)).length;
  const entropy = (s) => {
    if (!s) return 0;
    const m = {}; for (const ch of s) m[ch] = (m[ch] || 0) + 1;
    let h = 0; for (const v of Object.values(m)) { const p = v / s.length; h -= p * Math.log2(p); }
    return h;
  };
  let susp = 0; for (const w of Sayyad_SUSP) if (url.includes(w)) susp++;
  return [
    url.length, host.length, count(host, "."), count(host, "-"), digits(url), Math.max(0, labels.length - 2),
    /^\d{1,3}(\.\d{1,3}){3}$/.test(host) ? 1 : 0, hasAt, pathq.includes("//") ? 1 : 0, countAny(url, "?=&%_~"),
    https, susp, Sayyad_BAD_TLD.has(tld) ? 1 : 0, Sayyad_SHORT.has(host) ? 1 : 0, pathq.length, entropy(host),
    host ? digits(host) / host.length : 0
  ];
}