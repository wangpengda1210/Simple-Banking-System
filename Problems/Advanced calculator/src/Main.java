import java.util.Arrays;

/* Please, do not rename it */
class Problem {

    public static void main(String[] args) {
        String operator = args[0];
        // write your code here
        int[] values = new int[args.length - 1];
        for (int i = 0; i < values.length; i++) {
            values[i] = Integer.parseInt(args[i + 1]);
        }

        switch (operator) {
            case "MAX":
                Arrays.sort(values);
                System.out.println(values[values.length - 1]);
                break;
            case "MIN":
                Arrays.sort(values);
                System.out.println(values[0]);
                break;
            case "SUM":
                int sum = 0;
                for (int value : values) {
                    sum += value;
                }
                System.out.println(sum);
                break;
        }
    }
}