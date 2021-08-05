import java.io.File;  // Import the File class
import java.io.FileNotFoundException;  // Import this class to handle errors
import java.util.Scanner; // Import the Scanner class to read text files
import java.util.ArrayList; // import the ArrayList class
import java.util.Arrays;  
import java.util.List;  
import org.apache.commons.lang3.StringUtils;

public class hello{

    public static void main(String[] args){

        class film{
            private String type;
            private String name;
            private int year;
            private String director;
            private String subject;

            public film(String type, String name, int year, String director, String subject){
                this.type = type;
                this.name = name;
                this.year = year;
                this.director = director;
                this.subject = subject;
            }
        }

        ArrayList<film> films = new ArrayList<film>(1);
        
        try{
            File myObj = new File("textfile.txt");
            Scanner myReader = new Scanner(myObj);
            while(myReader.hasNextLine()){
                String data = myReader.nextLine();
                System.out.println(data);
                String[] arrayOfString = data.split("-");

                String tempType = arrayOfString[0];
                String tempName = arrayOfString[1];
                int tempYear = Integer.parseInt(arrayOfString[2]);
                String tempDirector = arrayOfString[3];
                String tempSubject = arrayOfString[4];
                film obj = new film(tempType,tempName,tempYear,tempDirector,tempSubject);
                films.add(obj);

            }
            myReader.close();
        }

        catch (FileNotFoundException e){
            System.out.println("An error occured.");
            e.printStackTrace();
        }

        boolean done = false;

        while(!done){
            try{
                    System.out.print("Enter your command: ");
                    String input = System.console().readLine();

                    String[] arrayofStringInput = input.split(" ");
                    System.out.println(arrayofStringInput[0]);
                    System.out.println(arrayofStringInput[1]);
                    System.out.println(arrayofStringInput[2]);


                    if(arrayofStringInput[0].equals("list") && arrayofStringInput[1].equals("film")){
                        if(arrayofStringInput[2].equals("-all")){
                            //show all films
                            for(int i = 0; i < films.size(); i++){
                                System.out.println(i+films.get(i).name+films.get(i).year+films.get(i).director+films.get(i).subject);
                                }
                        }
                        else if(arrayofStringInput[2].equals("-name")){
                            //show by name filter
                            for(int i = 0; i < films.size(); i++){
                                if(films.get(i).name.contains(arrayofStringInput[3])){
                                    System.out.println(i+films.get(i).name+films.get(i).year+films.get(i).director+films.get(i).subject);
                                }
                            }                        
                        }
                        else if(arrayofStringInput[2].equals("-year")){
                            //show by year filter
                            for(int i = 0; i < films.size(); i++){
                                if(films.get(i).year == Integer.parseInt(arrayofStringInput[3])){
                                    System.out.println(i+films.get(i).name+films.get(i).year+films.get(i).director+films.get(i).subject);
                                }
                            }    
                        }
                        else if(arrayofStringInput[2].equals("-genre")){
                            //show by genre filter
                            for(int i = 0; i < films.size(); i++){
                                if(films.get(i).subject.contains(arrayofStringInput[3])){
                                    System.out.println(i+films.get(i).name+films.get(i).year+films.get(i).director+films.get(i).subject);
                                }
                            }                                
                        }
                        else if(arrayofStringInput[2].equals("-director")){
                            //show by genre filter
                            for(int i = 0; i < films.size(); i++){
                                if(films.get(i).director.contains(arrayofStringInput[3])){
                                    System.out.println(i+films.get(i).name+films.get(i).year+films.get(i).director+films.get(i).subject);
                                }
                            }    
                        }
                    }
                    else if(arrayofStringInput[0].equals("insert") && arrayofStringInput[1].equals("film")){
                        String lineofText = String.join(" ",arrayofStringInput);
                        String[] valueBetweenQuotes = StringUtils.substringsBetween(lineofText,"*","*");
                        String[] splitted = valueBetweenQuotes[0].split("-");

                        String tempType = splitted[0];
                        String tempName = splitted[1];
                        int tempYear = Integer.parseInt(splitted[2]);
                        String tempDirector = splitted[3];
                        String tempSubject = splitted[4];    
                        film obj = new film(tempType,tempName,tempYear,tempDirector,tempSubject);
                        films.add(obj);
                    
                    }
                    else if(arrayofStringInput[0].equals("delete") && arrayofStringInput[1].equals("film")){
                        for(int i = 0; i < films.size(); i++){
                            String s="";
                            String str2 = "";

                            if(arrayofStringInput.length > 3){
                                for(int j = 2; j < arrayofStringInput.length; j++){
                                    s = s + arrayofStringInput[j] + " ";

                                    String[] str = s.split(" ");
                                    str2 = String.join(" ",str); 
                                }
                                System.out.println(str2);
                            }
                            else{
                                str2 = arrayofStringInput[2];
                            }

                            if(films.get(i).name.equals(str2)){
        
                                System.out.println(films.get(i).name+"sdfds");
                                films.remove(i);
                            }
                            else{
                                continue;
                            }
                        }
                    }
            }
            catch(Exception e){
                System.out.println("Something went wrong.");
            }

        }   

    }

}
