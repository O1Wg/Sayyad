import java.io.*;
import java.util.*;

public class BuildArff {
    public static void main(String[] a) throws Exception {
        int limit = a.length > 0 ? Integer.parseInt(a[0]) : Integer.MAX_VALUE;
        List<String> phish = new ArrayList<>(), legit = new ArrayList<>();
        Set<String> seen = new HashSet<>();
        try (BufferedReader r = new BufferedReader(new FileReader("data/malicious_phish.csv"))) {
            String line = r.readLine();                       // header
            while ((line = r.readLine()) != null) {
                int i = line.lastIndexOf(',');
                if (i < 0) continue;
                String url = line.substring(0, i).replace("\"", "").trim();
                String type = line.substring(i + 1).trim();
                if (!seen.add(url)) continue;
                if (type.equals("bad")) phish.add(url);
                else if (type.equals("good")) legit.add(url);
            }
        }
        System.out.println(phish.size() + " phishing and " + legit.size() + " legit");
        Collections.shuffle(phish, new Random(1));
        Collections.shuffle(legit, new Random(1));
        int n = Math.min(Math.min(phish.size(), legit.size()), limit);
        try (PrintWriter w = new PrintWriter("data/features.arff")) {
            w.println("@relation Sayyad");
            for (String name : Features.NAMES) w.println("@attribute " + name + " numeric");
            w.println("@attribute label {phishing,legit}");
            w.println("@data");
            for (int k = 0; k < n; k++) { write(w, phish.get(k), "phishing"); write(w, legit.get(k), "legit"); }
        }
        System.out.println(n + " phishing + " + n + " legit written to data/features.arff");
    }
    static void write(PrintWriter w, String url, String label) {
        StringBuilder sb = new StringBuilder();
        for (double v : Features.extract(url)) sb.append(v).append(',');
        w.println(sb.append(label));
    }
}