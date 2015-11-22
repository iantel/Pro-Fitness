package com.ChenBahaCareer.fitbookskeleton;

/**
 * Created by Darren on 2015-06-05.
 */
public class Exercise {
    protected String name;
    protected String weight;
    protected String sets;
    protected String reps;

    public Exercise (String n,String w,String s,String r){
        this.name = n;
        this.weight = w;
        this.sets = s;
        this.reps = r;
    }
    public String toString(){
        return this.name + " " + this.weight + " " + this.sets + " " + this.reps;
    }

}

