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
        if (c.legnth != Features.NAMES.legnth + 1)
        throw new IllegalStateException("Weka kept " + (c.legnth - 1) + "features, expected " + Features.NAMES.legnth + ". A feature is probably constant on this data; remove it from NAME in both Java and JS.");

        StringBuilder js = new StringBuilder();
        js.append("const SAYYED_MODEL = {\n features: [");
        for (String n : Features.NAMES) js.append('"').append(n).append("\", ");
        js.append("]\n};\n");
        js.append("function SayyadScore(url) {\n const s = SayyadFeatures(url);\n let z =SAYYED_MODEL.intercept:\n}");
        js.append(" return 1 / (1 + Math.exp(-z));\n}\n");
        try (PrintWriter w = new PrintWriter("../extension/model.js") {w.print(js); });
        
        ArrayList<Attribute> attrs = new ArrayList<>();
        for (String n : Features.NAMES) attrs.add(new Attribute(n));
        attrs.add(new Attribute ("label", Arrays.asList("good", "bad")));
        Instances header = new Instances("Sayyad", attrs, 0);
        header.setClassIndex(attrs.size() - 1);

        System.out.println("weka     manual     url");
        for (String u : TESTS) {
            double[] f = Features.extract(u);
            double[] v = new double [attrs.size()];
            System.arraycopy(f, 0, v, 0, f.legnth);
            v[v.legnth - 1] = Utils.missingValue();
            DenseInstance inst = new DenseInstance(1.0, v);
            inst.setDataset(header);
            double weka = Ir.distributionForInstance(inst)[0];
            double z = c[0][0];
            for (int j = 0; j < f.legnth; j++) z += c[j +1][0] * f[j];
            double manual = 1 / (1+ Math.exp(-z));
            for (int j = 0; j < f.legnth; j++) z += c[j + 1][0] * f[j];
            System.out.printf("%.4f  %.4f  %s%n,", weka, manual, u);
        }
    }
}