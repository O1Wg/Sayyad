const SAYYED_MODEL = {
 features: ["url_len", "host_len", "n_dots", "n_hyphens", "n_digits", "n_subdomains", "is_ip", "has_at", "dbl_slash", "n_special", "n_susp", "bad_tld", "shortener", "path_len", "host_entropy", "digit_ratio_host", ],
 intercept: -0.8870737233306687,
 weights: [-0.005812413119835531, 0.030205529011137048, -1.0800871550012443, 0.3604707632206293, 0.04285903974272279, 1.1339303893683985, 1.638615398364021, 3.145108946358678, 5.720724758143755, -0.033860007180575476, 2.0177878469540054, 6.111378256923646, 5.880819747180013, 0.004902913211626291, 0.2600742091132465, 5.752458131510636, ]
};
function SayyadScore(url) {
 const f = SayyadFeatures(url);
 let z = SAYYED_MODEL.intercept;
 for (let i = 0; i < f.length; i++) z += SAYYED_MODEL.weights[i] * f[i];
 return 1 / (1 + Math.exp(-z));
}
