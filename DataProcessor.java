import java.util.ArrayList;
import java.util.Scanner;

// 定义 DataResult 类，用于封装数据和处理结果
class DataResult {
    private String name; // 数据集名称
    private ArrayList<Double> data; // 数据
    private double average; // 平均值
    private double aError; // A类误差
    private double bError; // B类误差
    private double totalError; // 总误差
    private double relativeUncertainty; // 相对不确定度

    // 构造函数
    public DataResult(String name) {
        this.name = name;
        this.data = new ArrayList<>();
    }

    // Getter 和 Setter 方法
    public String getName() {
        return name;
    }

    public ArrayList<Double> getData() {
        return data;
    }

    public double getAverage() {
        return average;
    }

    public void setAverage(double average) {
        this.average = average;
    }

    public double getAError() {
        return aError;
    }

    public void setAError(double aError) {
        this.aError = aError;
    }

    public double getBError() {
        return bError;
    }

    public void setBError(double bError) {
        this.bError = bError;
    }

    public double getTotalError() {
        return totalError;
    }

    public void setTotalError(double totalError) {
        this.totalError = totalError;
    }

    public double getRelativeUncertainty() {
        return relativeUncertainty;
    }

    public void setRelativeUncertainty(double relativeUncertainty) {
        this.relativeUncertainty = relativeUncertainty;
    }

    // 打印结果
    public void printResult() {
        System.out.println("数据集名称：" + name);
        System.out.println("数据的平均值为：" + average);
        System.out.println("数据的A类误差为：" + aError);
        System.out.println("数据的B类误差为：" + bError);
        System.out.println("数据的总误差为：" + totalError);
        System.out.println("数据的相对不确定度为：" + relativeUncertainty);
    }
}

public class DataProcessor {
    private Scanner scanner = new Scanner(System.in);

    // 计算平均值
    public static double calculateAverage(ArrayList<Double> data) {
        if (data.isEmpty()) {
            throw new IllegalArgumentException("数据为空，无法计算平均值。");
        }
        double sum = 0;
        for (double num : data) {
            sum += num;
        }
        return sum / data.size();
    }

    // 计算A类误差
    public static double calculateError(ArrayList<Double> data, double average) {
        if (data.isEmpty()) {
            throw new IllegalArgumentException("数据为空，无法计算误差。");
        }
        double sumOfSquares = 0;
        for (double num : data) {
            sumOfSquares += Math.pow(num - average, 2);
        }
        return Math.sqrt(sumOfSquares / (data.size() * (data.size() - 1)));
    }

    // 获取仪器误差
    public double getInstrumentalError() {
        while (true) {
            System.out.println("请输入仪器误差：");
            String input = scanner.next();
            try {
                double instrumentalError = Double.parseDouble(input);
                return instrumentalError / Math.sqrt(3);
            } catch (NumberFormatException e) {
                System.out.println("无效输入，请输入数字。");
            }
        }
    }

    // 计算总误差
    public double calculateTotalError(double aError, double bError) {
        return Math.sqrt(Math.pow(aError, 2) + Math.pow(bError, 2));
    }

    // 输入数据
    public DataResult inputData(String name) {
        DataResult result = new DataResult(name); // 创建新的 DataResult 对象
        System.out.println("当前数据集：" + name);
        System.out.println("请输入实验数据（输入 'end' 结束输入）：");
        while (true) {
            String input = scanner.next();
            if (input.equalsIgnoreCase("end")) {
                break;
            }
            try {
                double value = Double.parseDouble(input);
                result.getData().add(value);
                System.out.println("已添加数据：" + value);
            } catch (NumberFormatException e) {
                System.out.println("无效输入，请输入数字或 'end' 结束输入。");
            }
        }

        if (result.getData().isEmpty()) {
            System.out.println("未输入任何数据，跳过当前数据集。");
            return null;
        }

        // 计算并设置结果
        double average = calculateAverage(result.getData());
        double aError = calculateError(result.getData(), average);
        double bError = getInstrumentalError();
        double totalError = calculateTotalError(aError, bError);
        double relativeUncertainty = totalError / average;

        result.setAverage(average);
        result.setAError(aError);
        result.setBError(bError);
        result.setTotalError(totalError);
        result.setRelativeUncertainty(relativeUncertainty);

        return result;
    }

    // 询问用户是否继续
    public boolean askToContinue() {
        while (true) {
            System.out.println("是否继续处理数据？(yes/no)");
            String input = scanner.next().trim().toLowerCase();
            if (input.equals("yes")) {
                return true;
            } else if (input.equals("no")) {
                return false;
            } else {
                System.out.println("无效输入，请输入 'yes' 或 'no'。");
            }
        }
    }

    public static void main(String[] args) {
        DataProcessor processor = new DataProcessor();
        boolean continueProcessing = true;
        int datasetCount = 1; // 数据集计数器

        while (continueProcessing) {
            String datasetName = "Dataset " + datasetCount; // 生成数据集名称
            DataResult result = processor.inputData(datasetName);

            if (result != null) {
                result.printResult(); // 打印结果
                datasetCount++; // 更新数据集计数器
            }

            continueProcessing = processor.askToContinue();
        }

        System.out.println("程序结束。");
        processor.scanner.close();
    }
}