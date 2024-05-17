package org.dgrytsyna.task3;

import java.util.ArrayList;
import java.util.List;

public class CommonWord {

    private List<String> documentNames = new ArrayList<>();
    private Integer count;

    public CommonWord(List<String> documentNames, Integer count) {
        for(int i=0; i<documentNames.size(); i++){
            this.documentNames.add(documentNames.get(i));
        }
        this.count = count;
    }

    public List<String> getDocumentNames() {
        return documentNames;
    }

    public Integer getCount() {
        return count;
    }

    public void incrementCount(){
        count++;
    }

    public  void addList(List<String> newDocuments){
        for(String document:newDocuments){
            if(!documentNames.contains(document))documentNames.add(document);
        }
    }
    public void addCounter(Integer newCounter){
        count += newCounter;
    }
    public void setCount(Integer count) {
        this.count = count;
    }

    public void printInfo(){
        System.out.print(" word found in documents: " + documentNames + " count " + count );
    }
}
