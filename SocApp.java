import java.util.*;
import java.io.File;
import java.io.FileNotFoundException;

public class SocApp
{
   private DiGraph myNetwork = new DiGraph();
   private List<String> users = new ArrayList<>();
   
   public SocApp(String f) throws FileNotFoundException
   {
   
      //Assign passed filename to var
      String fname = f;
   
     //Attempt to read in file
      try
      {
         File inputFile = new File(fname);
         Scanner inFile = new Scanner(inputFile);
         inFile.close();
      }
      catch(FileNotFoundException x)
      {
         //File could not be read successfully
         System.out.println("Error! File Could Not Be Located.");
      }
   
         //Read in file
         File inputFile = new File(fname);
         Scanner inFile = new Scanner(inputFile);
         
         users = new ArrayList<>();
         users.add("dummy");
         
         while(inFile.hasNext())
         {
            String follower = inFile.next();
            String followed = inFile.next();
            
            
            if(users.indexOf(follower) == -1)
            {
               users.add(follower);
               myNetwork.addVertex(users.indexOf(follower));
            }
            
            if(users.indexOf(followed) == -1)
            {
               users.add(followed);
               myNetwork.addVertex(users.indexOf(followed));
            }
            
            myNetwork.addEdge(users.indexOf(follower), users.indexOf(followed));
         }
         
         users.add("dummy");
                  
         //Close file
         inFile.close();
   }
   
   private int[] getFollowers()
   {
      Set<Integer> count;
      int[] followCount = new int[users.size()];
      
      for(int i = 1; i < users.size()-1; i++)
      {
         count = myNetwork.getAdjacent(i);
         
         for(int each : count)
         {
            followCount[each] += 1;
         }
      }
      
      return followCount;
   
   }
   
   
   public String mostPopular()
   {
      int coolestPerson = 0;
      int popFollowers[] = this.getFollowers();
      int j = 1;
      int highestNum = 0;
      
      for(int ea : popFollowers)
      {
         if(ea > highestNum)
         {
            highestNum = ea;
            coolestPerson = j;
            j++;
         }
      }
      return users.get(coolestPerson);
   }
   
   
   public String topFollower()
   {
      int maxFollow = 0;
      int topFollow = 1;
   
      for(int i = 1; i < users.size()-1; i++)
      {
         if(myNetwork.getAdjacent(i).size() >= maxFollow)
         {
            maxFollow = myNetwork.getAdjacent(i).size();
            topFollow = i;
         }
      }
      
      return users.get(topFollow);
   }
   
   
   public boolean isInfluencer(String u)
   { 
      int[] inFollowers = this.getFollowers();
      double userF = inFollowers[users.indexOf(u)];
      double totalF = users.size() - 2;
      
      if(userF/totalF > 0.39)
      {
         return true;
      } 
      return false;
   }
   
   
   
   public double reciprocity()
   {
      double rEdges = 0.0;
      
      for(int i = 1; i < users.size()-1; i++)
      {
         Set<Integer> thisUser = myNetwork.getAdjacent(i);
         
         for(int each : thisUser)
         {
            if(myNetwork.getAdjacent(each).contains(i))
            {
               rEdges += 1.0;
            }
         }
      }
      return rEdges/myNetwork.edges();
   }
   
   private int[] neighbors(String n)
   {
      int user1 = users.indexOf(n);
      
      List<Integer> visited = new ArrayList<>();
      
      //Indices represent user vertex, the number stored there represents the parent/previouse vertex used to arrive there. 0 represents an unaccessible vertex.
      int[] tracker = new int[myNetwork.vertices()+1];
      
      Queue<Integer> lineup = new LinkedList<>();
      
      lineup.add(user1);
      visited.add(lineup.peek());
      
      while(!lineup.isEmpty())
      {
         int parent = lineup.peek();
         
         for(int each : myNetwork.getAdjacent(lineup.peek()))
         {
            if(!visited.contains(each))
            {
               lineup.add(each);
               tracker[each] = parent;
               visited.add(each);
            } 
         }
         lineup.remove();
      }
      return tracker;
   
   }
   
   
   public int distance(String u1, String u2)
   {
      int[] neighbors = this.neighbors(u1);
      int leaf = users.indexOf(u2);
      boolean found = false;
      int distance = 0;
      if(neighbors[leaf] != 0)
      {
         int next = neighbors[leaf];
         while(!found)
         {
            if(neighbors[next] == 0)
            {
               distance++;
               found = true;
            }
            else
            {
               distance++;
               next = neighbors[next];
            }
         }
      }
      else
      {
         distance = Integer.MAX_VALUE;
      }
      
      return distance;
   
   }
   
   
   public String path(String u1, String u2)
   {
      int[] neighborsP = this.neighbors(u1);
      int leafP = users.indexOf(u2);
      boolean foundP = false;
      Stack<String> revPath = new Stack<>();
      String path = "[";
      if(neighborsP[leafP] != 0)
      {
         int nextP = neighborsP[leafP];
         
         revPath.push(users.get(leafP));
         revPath.push(users.get(neighborsP[leafP]));
         
         while(!foundP)
         {
            if(neighborsP[nextP] == 0)
            {
               foundP = true;
            }
            else
            {
               revPath.push(users.get(neighborsP[nextP]));
               nextP = neighborsP[nextP];
            }
         }
      }
      else
      {
         return "[NONE]";
      }
      
      while(!revPath.empty())
      {
         path += revPath.pop();
         
         if(!revPath.empty())
         {
            path += "|";
         }
      }
      
      return path + "]";
   
   }
   
   
   public String reachable(String u)
   {
      String username = u;
      Set<Integer> reach = myNetwork.getAdjacent(users.indexOf(username));
      String reachable = "[";
      boolean first = true;
      
      for(int each : reach)
      {
         if(first)
         {
            reachable += users.get(each);
            first = false;
         }
         else
         {
            reachable += ", " + users.get(each);
         }
      }
      
      return reachable + "]";
      
   
   }


}