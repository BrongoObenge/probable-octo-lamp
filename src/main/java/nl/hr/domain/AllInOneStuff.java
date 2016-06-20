package nl.hr.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Created by j on 6/20/16.
 */

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AllInOneStuff<T> {
    private T x;
    private T y;
    private T predictedY;
    private T alpha;
    private T beta;
    private T errors;
    private T sumOfSquaredErrors;
}
