package nl.hr.domain;

/**
 * Created by j on 6/20/16.
 */
public class OwnMath {

    public double pow (double a, int toThePower) {
        if ( toThePower == 0)        return 1;
        if ( toThePower == 1)        return a;
        if (isEven( toThePower ))    return     pow ( a * a, toThePower/2); //even a=(a^2)^b/2
        else                return a * pow ( a * a, toThePower/2); //odd  a=a*(a^2)^b/2

    }
    private boolean isEven(long a){
        if(a%2 ==0) return true;
        return false;
    }
}
