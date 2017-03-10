package test1.nh.com.demos1.stringTest;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by Administrator on 15-12-30.
 */
public class RegexTest1 {


    //  A regular expression is a textual pattern used to search in text.

    @Before
    public void before(){

    }

    @After
    public void after(){

    }


    @Test
    public void test1(){
        String text ="This is the text to be searched for occurrences of the http:// pattern.";
        String pattern = ".*http://.*";    // .* -> one or more characters
        boolean matches = Pattern.matches(pattern, text);
        System.out.println("matches = " + matches);
    }

    @Test
    public void test2(){
        String text ="This is the text which is to be searched for occurrences of the word 'is'.";

        String patternString = "is";

        Pattern pattern = Pattern.compile(patternString);
        Matcher matcher = pattern.matcher(text);

        int count = 0;
        while(matcher.find()) {
            count++;
            System.out.println("found: " + count + " : "+matcher.start() + " - " + matcher.end());
        }
    }

    @Test
    public void matcherTest(){
        String text="This is the text to be searched for occurrences of the http:// pattern.";
        String patternString = ".*http://.*";
        Pattern pattern = Pattern.compile(patternString, Pattern.CASE_INSENSITIVE);
        Matcher matcher = pattern.matcher(text);
        boolean matches = matcher.matches();
        System.out.println("matches = " + matches);
    }

    @Test
    public void materTest2(){
        String text = "This is the text to be searched for occurrences of the http:// pattern.";

        String patternString = "This is the";

        Pattern pattern = Pattern.compile(patternString, Pattern.CASE_INSENSITIVE);
        Matcher matcher = pattern.matcher(text);

        System.out.println("lookingAt = " + matcher.lookingAt());
        System.out.println("matches   = " + matcher.matches());   // The regular expression says that the text must match the text "This is the" exactly

    }

    @Test
    public void matcherTest3(){
        String text="AABB";

        Pattern p = Pattern.compile("(\\d)\\1(\\d)\\2");
        Matcher m = p.matcher(text);
        System.out.println(m.matches());


    }





    @Test
    public void splitTest(){
        String text = "A-sep Text sep With sep Many sep Separators";

        String patternString = "sep";
        Pattern pattern = Pattern.compile(patternString);

        String[] split = pattern.split(text);

        System.out.println("split.length = " + split.length);
        for(String element : split){
            System.out.println("element = " + element);
        }
    }





}
