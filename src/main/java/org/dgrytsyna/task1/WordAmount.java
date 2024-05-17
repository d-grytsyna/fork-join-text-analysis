package org.dgrytsyna.task1;

public class WordAmount {
    Integer count = 0;
    Integer sumLength = 0;


    public Integer getSumLength() {
        return sumLength;
    }


    public void setSumLength(Integer sumLength) {
        this.sumLength = sumLength;
    }

    public Integer getCount() {
        return count;
    }

    public void setCount(Integer count) {
        this.count = count;
    }

    public void addCount(Integer count){
        this.count += count;
    }

    public void addLength(Integer length){
        this.sumLength +=length;
    }
}
