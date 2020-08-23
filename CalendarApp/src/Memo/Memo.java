package Memo;

import AppID.KeyedEntity;

import java.io.Serializable;
import java.time.LocalDateTime;


/**
 * Class for the memo Entity
 * <p>
 * Memos contain strings and are intended to be used as notes on a(n) Event(s)
 *
 * @ author Robert Girard
 */
public class Memo extends KeyedEntity implements Serializable {
    private String memoString;
    private final LocalDateTime creationTime;

    /**
     * constructs a Memo without author
     *
     * @param memoString content of the Memo
     */
    public Memo(String memoString) {
        this.memoString = memoString;
        this.creationTime = LocalDateTime.now();
    }

    /**
     * convert a Memo to a String to be displayed when needed
     *
     * @return a Memo in String format
     */
    @Override
    public String toString() {
        return "memoString='" + memoString + '\'' +
                ", creationTime=" + creationTime;
    }

    /**
     * Setter for the memos string
     *
     * @param memoString the string that the memo will contain
     */
    void setMemoString(String memoString) {
        this.memoString = memoString;
    }


}
