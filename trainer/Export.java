import java.io.*;
import java.util.*;
import weka.core.*;
import weka.classifiers.functions.Logistic;

public class Export{
    static final String[] TESTS = {
        "https://www.google.com/",
        "http://absher-verify.xyz/login",
        "http://192.168.1.10/secure/update.php",
        "https://github.com/O1Wg/Sayyad",
        "http://alrajhi.secure-login-update.top/account/confirm?id=1"
    };
    public static void main(String[] a) throws Exception{
        Logistic Ir = (Logistic) SerializationHelper.read("models/Sayyad-logistic.model");
        double [][] c = Ir.coefficients(); // c[0] = intercept, c[j] = feature j (1-based);
        if (c.length != Features.NAMES.length + 1)
        throw new IllegalStateException("Weka kept " + (c.length - 1) + "features, expected " + Features.NAMES.length + ". A feature is probably constant on this data; remove it from NAME in both Java and JS.");

        StringBuilder js = new StringBuilder();
        js.append("const SAYYED_MODEL = {\n features: [");
        for (String n : Features.NAMES) js.append('"').append(n).append("\", ");
               js.append("],\n intercept: ").append(c[0][0]).append(",\n weights: [");
        for (int j = 1; j < c.length; j++) js.append(c[j][0]).append(", ");
        js.append("]\n};\n");
        js.append("function SayyadScore(url) {\n const f = SayyadFeatures(url);\n let z = SAYYED_MODEL.intercept;\n");
        js.append(" for (let i = 0; i < f.length; i++) z += SAYYED_MODEL.weights[i] * f[i];\n");
        js.append(" return 1 / (1 + Math.exp(-z));\n}\n");
        try (PrintWriter w = new PrintWriter("../extension/model.js")) {w.print(js); };
        
        ArrayList<Attribute> attrs = new ArrayList<>();
        for (String n : Features.NAMES) attrs.add(new Attribute(n));
        attrs.add(new Attribute ("label", Arrays.asList("phishing", "legit")));
        Instances header = new Instances("Sayyad", attrs, 0);
        header.setClassIndex(attrs.size() - 1);

        System.out.println("weka     manual     url");
        for (String u : TESTS) {
            double[] f = Features.extract(u);
            double[] v = new double [attrs.size()];
            System.arraycopy(f, 0, v, 0, f.length);
            v[v.length - 1] = Utils.missingValue();
            DenseInstance inst = new DenseInstance(1.0, v);
            inst.setDataset(header);
            double weka = Ir.distributionForInstance(inst)[0];
            double z = c[0][0];
            for (int j = 0; j < f.length; j++) z += c[j +1][0] * f[j];
            double manual = 1 / (1+ Math.exp(-z));
            for (int j = 0; j < f.length; j++) z += c[j + 1][0] * f[j];
            System.out.printf("%.4f  %.4f  %s%n", weka, manual, u);        
        }
    }
}