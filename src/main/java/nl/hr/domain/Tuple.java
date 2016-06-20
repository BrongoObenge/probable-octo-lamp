package nl.hr.domain;

import lombok.Value;

/**
 * Created by j on 6/20/16.
 */
@Value
public class Tuple<X, Y> {
    X _1;
    Y _2;
}
