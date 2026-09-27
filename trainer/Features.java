import java.util.*;

public class Features {
    public static final String[] NAMES = {
        "url_len", "host_len", "n_dots", "n_hyphens", "n_digits", "n_subdomains", "is_ip", "has_at",
        "dbl_slash", "n_special", "is_https", "n_susp", "bad_tld", "shortener", "path_len",
        "host_entropy", "digit_ratio_host"
    };
    static final String[] SUSP = {"login", "signin", "sign-in", "verify", "verification", "secure", "security",
        "account", "update", "confirm", "password", "bank", "free", "bonus", "gift", "win", "prize", "alert",
        "urgent", "suspend", "unlock", "wallet", "invoice", "payment", "pay", "otp", "support"};
    static final Set<String> BAD_TLD = new HashSet<>(Arrays.asList("xyz", "top", "tk", "ml", "ga", "cf", "gq",
        "icu", "buzz", "click", "work", "link", "zip", "mov", "cam", "rest", "monster", "quest", "fit", "lol"));
    static final Set<String> SHORT = new HashSet<>(Arrays.asList("bit.ly", "tinyurl.com", "t.co", "goo.gl",
        "cutt.ly", "is.gd", "ow.ly", "rebrand.ly", "shorturl.at", "tiny.cc", "rb.gy"));

    public static double[] extract(String raw) {
        String url = raw.trim().toLowerCase();
        int https = 0;
        String rest = url;
        if (url.startsWith("https://")) { https = 1; rest = url.substring(8); }
        else if (url.startsWith("http://")) { rest = url.substring(7); }
        int cut = rest.length();
        for (char c : new char[]{'/', '?', '#'}) { int i = rest.indexOf(c); if (i >= 0 && i < cut) cut = i; }
        String host = rest.substring(0, cut);
        String pathq = rest.substring(cut);
        int hasAt = url.contains("@") ? 1 : 0;
        int at = host.lastIndexOf('@'); if (at >= 0) host = host.substring(at + 1);
        int colon = host.indexOf(':'); if (colon >= 0) host = host.substring(0, colon);
        host = host.replaceAll("\\.+$", "");
        String[] labels = host.isEmpty() ? new String[0] : host.split("\\.");
        String tld = labels.length > 0 ? labels[labels.length - 1] : "";
        int susp = 0; for (String w : SUSP) if (url.contains(w)) susp++;

        double[] f = new double[NAMES.length];
        f[0] = url.length();
        f[1] = host.length();
        f[2] = count(host, '.');
        f[3] = count(host, '-');
        f[4] = countDigits(url);
        f[5] = Math.max(0, labels.length - 2);
        f[6] = host.matches("\\d{1,3}(\\.\\d{1,3}){3}") ? 1 : 0;
        f[7] = hasAt;
        f[8] = pathq.contains("//") ? 1 : 0;
        f[9] = countAny(url, "?=&%_~");
        f[10] = https;
        f[11] = susp;
        f[12] = BAD_TLD.contains(tld) ? 1 : 0;
        f[13] = SHORT.contains(host) ? 1 : 0;
        f[14] = pathq.length();
        f[15] = entropy(host);
        f[16] = host.isEmpty() ? 0 : (double) countDigits(host) / host.length();
        return f;
    }

    static int count(String s, char c) { int n = 0; for (char x : s.toCharArray()) if (x == c) n++; return n; }
    static int countDigits(String s) { int n = 0; for (char x : s.toCharArray()) if (x >= '0' && x <= '9') n++; return n; }
    static int countAny(String s, String set) { int n = 0; for (char x : s.toCharArray()) if (set.indexOf(x) >= 0) n++; return n; }
    static double entropy(String s) {
        if (s.isEmpty()) return 0;
        Map<Character, Integer> m = new HashMap<>();
        for (char c : s.toCharArray()) m.merge(c, 1, Integer::sum);
        double h = 0;
        for (int v : m.values()) { double p = (double) v / s.length(); h -= p * Math.log(p) / Math.log(2); }
        return h;
    }

    public static void main(String[] a) {
        String[] demo = {"https://www.google.com/", "http://absher-verify.xyz/login", "http://192.168.1.10/secure/update.php"};
        for (String u : demo) System.out.println(u + " -> " + Arrays.toString(extract(u)));
    }
}