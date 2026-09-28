const SAYYED_MODEL = {
 features: ["url_len", "host_len", "n_dots", "n_hyphens", "n_digits", "n_subdomains", "is_ip", "has_at", "dbl_slash", "n_special", "n_susp", "bad_tld", "shortener", "path_len", "host_entropy", "digit_ratio_host", ],
 intercept: 451.8477721477318,
 weights: [6.444996769950402, -6.426357639396185, -453.9133983591603, 0.39544407363498674, 0.0497682058609274, 454.0138066750316, 15.287243529067304, 3.2300895528238307, 20.897954144382613, -0.037346916218807896, 2.0034319888360117, 24.268899552504575, 55.95180305396702, -6.447111957798226, 0.3149569992521124, 6.094345728145964, ]
};
function SayyadScore(url) {
 const f = SayyadFeatures(url);
 let z = SAYYED_MODEL.intercept;
 for (let i = 0; i < f.length; i++) z += SAYYED_MODEL.weights[i] * f[i];
 return 1 / (1 + Math.exp(-z));
}
