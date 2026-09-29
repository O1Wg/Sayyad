function SayyadReasons(url, host) {
  const f = SayyadFeatures(url);
  const v = (name) => f[Sayyad_NAMES.indexOf(name)];
  const out = [];
  const brand = SayyadBrandCheck(host);
  if (brand) out.push(`Mentions "${brand}" but is not the real ${brand} site`);
  if (v("is_ip")) out.push("Uses a raw IP address instead of a name");
  if (v("has_at")) out.push("Contains @, which hides the real destination");
  if (v("shortener")) out.push("A link shortener hides where it goes");
  if (v("bad_tld")) out.push("Ends in a domain type common in scams");
  if (v("n_susp") >= 2) out.push("Uses words scammers rely on (login, verify, update)");
  if (v("n_subdomains") >= 3) out.push("Too many subdomains, hiding the real site");
  if (v("n_hyphens") >= 2) out.push("Hyphen-stuffed domain name");
  if (/^http:\/\//i.test(String(url).trim())) out.push("No HTTPS");   // a rule, not a model feature: the dataset has no schemes
  if (v("url_len") > 75) out.push("Unusually long address");
  return out.slice(0, 3);
}

function SayyadHost(url) {
  try { return new URL(url).hostname; } catch (e) { return ""; }
}

function SayyadVerdict(url) {
  const host = SayyadHost(url);
  const p = SayyadScore(url);
  const brand = SayyadBrandCheck(host);
  const trusted = SayyadTrusted(host);
  return { p, brand, trusted, risky: !trusted && (p >= 0.9 || !!brand) };
}