package support;

/**
 * Created by tiagoalexbastos on 01-06-2017.
 */
public class LineUpdate implements Comparable<LineUpdate>{
    private String line;
    private VectorTimestamp vt;

    public LineUpdate(String line, VectorTimestamp vt) {
        this.line = line;
        this.vt = vt;
    }

    public String getLine() {
        return line;
    }

    public VectorTimestamp getVt() {
        return vt;
    }

    @Override
    public int compareTo(LineUpdate update) {
        int[] vt1 = this.vt.toIntArray();
        int[] vt2 = update.vt.toIntArray();

        boolean vt1_greater = false;
        boolean vt2_greater = false;
        boolean vts_equal = false;

        for(int i = 0; i < vt1.length; i++) {
            if(vt1[i] > vt2[i])
                vt1_greater = true;

            if(vt1[i] <= vt2[i]) {
                if(vt1[i] < vt2[i])
                    vt2_greater = true;
                else
                    vts_equal = true;
            }
        }

        if(vt1_greater && vt2_greater)
            return 0;
        else if(vts_equal && !vt1_greater)
            return 1;
        else
            return -1;
    }

    @Override
    public String toString() {
        return line;
    }
}
