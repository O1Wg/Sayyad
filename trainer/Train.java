import java.util.Random;
import weka.core.*;
import weka.core.converters.ConverterUtils.DataSource;
import weka.classifiers.Evaluation;
import weka.classifiers.functions.Logistic;
import weka.classifiers.trees.RandomForest;

public class Train {
    public static void main(String[] a) throws Exception {
        Instances data = DataSource.read("data/features.arff");
        data.setClassIndex(data.numAttributes() - 1);
        data.randomize(new Random(1));
        int nTrain = (int) (data.numInstances() * 0.8);
        Instances train = new Instances(data, 0, nTrain);
        Instances test = new Instances(data, nTrain, data.numInstances() - nTrain);

        Logistic lr = new Logistic();
        lr.buildClassifier(train);
        Evaluation ev = new Evaluation(train);
        ev.evaluateModel(lr, test);
        System.out.println("=== Logistic regression, held-out 20% ===");
        System.out.println(ev.toSummaryString());
        System.out.println(ev.toClassDetailsString());
        System.out.println(ev.toMatrixString());
        SerializationHelper.write("models/Sayyad-logistic.model", lr);

        RandomForest rf = new RandomForest();
        rf.setNumIterations(100);
        rf.buildClassifier(train);
        Evaluation ev2 = new Evaluation(train);
        ev2.evaluateModel(rf, test);
        System.out.println("=== Random forest, held-out 20% ===");
        System.out.println(ev2.toSummaryString());
        System.out.println(ev2.toClassDetailsString());
        SerializationHelper.write("models/Sayyad-rf.model", rf);
    }
}