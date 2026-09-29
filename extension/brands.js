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
