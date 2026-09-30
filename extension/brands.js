const Sayyad_BRANDS = {
  absher: ["absher.sa"],
  nafath: ["nafath.sa"],
  alrajhi: ["alrajhibank.com.sa"],
  alinma: ["alinma.com"],
  riyadbank: ["riyadbank.com"],
  alahli: ["alahli.com"],
  mobily: ["mobily.com.sa"],
  stcpay: ["stcpay.com.sa"],
  aramco: ["aramco.com"],
  tawakkalna: ["ta.sdaia.gov.sa"]
};

function SayyadBrandCheck(host) {
  host = String(host || "").toLowerCase();
  const tokens = host.split(/[.-]/);
  for (const [brand, domains] of Object.entries(Sayyad_BRANDS)) {
    const mentioned = tokens.some(t => t === brand || (brand.length >= 5 && t.startsWith(brand)));
    const real = domains.some(d => host === d || host.endsWith("." + d));
    if (mentioned && !real) return brand;
  }
  return null;
}
// this is a list of known trusted domains, including major tech companies and government/educational institutions in Saudi Arabia. The SayyadTrusted function checks if a given host is in this trusted list or matches any of the known brands' domains.
const Sayyad_TRUSTED = [
  "google.com", "google.com.sa", "youtube.com", "gmail.com",
  "microsoft.com", "live.com", "office.com", "apple.com", "icloud.com",
  "github.com", "paypal.com", "amazon.com", "amazon.sa",
  "x.com", "twitter.com", "instagram.com", "facebook.com", "whatsapp.com",
  "snapchat.com", "tiktok.com", "linkedin.com", "wikipedia.org",
  "netflix.com", "stackoverflow.com", "gov.sa", "edu.sa"
];

function SayyadTrusted(host) {
  host = String(host || "").toLowerCase();
  const known = Sayyad_TRUSTED.concat(...Object.values(Sayyad_BRANDS));
  return known.some(d => host === d || host.endsWith("." + d));
}
