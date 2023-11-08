import java.util.*;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;



class Song implements Serializable{
    private String title;
    private String artist;
    private int duration;
    private int index;

  public Song(String title, String artist, int duration, int index) {
        this.title = title;
        this.artist = artist;
        this.duration = duration;
        this.index = index;
    }
    public int getIndex() {
        return index;
    }


  public String toString() {
    return title + " by " + artist + " (" + duration + " seconds)";
  }
}

class Musicplayer {
  private List<Song> listofsongs = new LinkedList<>();
  private List<Song> queue = new LinkedList<>();
   private List<Integer> queueIndices = new ArrayList<>();

  private int SongIndex = -1;
  private String playMode = "Repeat all";
  

  public void addsong(Song song) {
    listofsongs.add(song);
  }

  public void ShowPlaylist() {
    int SongNumber = 1;
    for (Song song : listofsongs) {
      System.out.println(SongNumber + "." + song);
      SongNumber++;
    }
  }
   public void QueueAdd(int songIndex) {
        if (queueIndices.contains(songIndex)) {
            System.out.println("Song is already in the queue.");
        } else {
            queueIndices.add(songIndex);
            System.out.println("Added song to the queue: " + listofsongs.get(songIndex).toString());
        }
    }

    public void QueueRemove(int songIndex) {
        if (queueIndices.contains(songIndex)) {
            queueIndices.remove(Integer.valueOf(songIndex));
            System.out.println("Removed song from the queue: " + listofsongs.get(songIndex).toString());
        } else {
            System.out.println("Song not found in the queue.");
        }
    }


    public void QueueEdit(String action, String value) {
        if (action.equalsIgnoreCase("add")) {
            List<Song> matchingSongs = SearchSongs(value);
            if (!matchingSongs.isEmpty()) {
                Song matchingSong = matchingSongs.get(0);
                int songIndex = listofsongs.indexOf(matchingSong);
                QueueAdd(songIndex);
            } else {
                System.out.println("Song not found.");
            }
        } else if (action.equalsIgnoreCase("remove")) {
            List<Song> matchingSongs = SearchSongs(value);
            if (!matchingSongs.isEmpty()) {
                Song matchingSong = matchingSongs.get(0);
                int songIndex = listofsongs.indexOf(matchingSong);
                QueueRemove(songIndex);
            } else {
                System.out.println("Song not found in the queue.");
            }
        } else {
            System.out.println("Invalid action. Use 'add' or 'remove'.");
        }
    }
  public void NowPlaying() {
    if (SongIndex >= 0 && SongIndex < queue.size()) {
      System.out.println("Song playing " + queue.get(SongIndex));
    } else {
      System.out.println("Currently no song is playing ");
    }
  }

  public List<Song> SearchSongs(String keyword) {
    List<Song> MatchingSong = new LinkedList<>();
    for (Song song : listofsongs) {
      if (song.toString().toLowerCase().contains(keyword.toLowerCase())) {
        MatchingSong.add(song);
      }
    }
    return MatchingSong;
  }

  public void QueueOfSong(String keyword) {
    List<Song> MatchingSong = SearchSongs(keyword);
    if (MatchingSong.isEmpty()) {
      System.out.println("NO MATCHING SONG FOUND");
    } else {
      queue.clear();
      queue.addAll(MatchingSong);
      SongIndex = 0;
      System.out.println("Queue Created Succesfully. now playing: ");
      NowPlaying();
    }
  }

  // public void QueueEdit(String action, String value) {
  //   if (action.equals("ADD")) {
  //     List<Song> MatchingSong = SearchSongs(value);
  //     if (MatchingSong.isEmpty()) {
  //       System.out.println("Songs not found to add to the queue");
  //     } else {
  //       queue.addAll(MatchingSong);
  //       System.out.println("\nAdded songs succesfully.");
  //     }
  //   } else if (action.equals("REMOVE")) {
  //     try {
  //       int SongNumber = Integer.parseInt(value);
  //          System.out.println("SongNumber: " + SongNumber); 
  //       if (SongNumber >= 0 && SongNumber < queue.size()) {
  //         Song RemovedSong = queue.remove(SongNumber - 1);
  //         System.out.println("Removed: " + RemovedSong);
  //       } else {
  //         System.out.println("Invalid input!");
  //       }
  //     } catch (Exception e) {
  //       System.out.println("Enter a valid Song Number");
  //     }
  //   }


  public void ChangeMode(String mode) {
    if (mode.equals("Repeat the same Song") || mode.equals("Repeat All Songs") || mode.equals("Shuffle the playlist")) {
      playMode = mode;
      System.out.println("Play mode set to: " + mode);
    } else {
      System.out.println("Invalid playing mode,please choose Between the Available Modes");
    }
  }

  public void NextSong() {
    if (playMode.equals("Repeat the same Song")) {
      System.out.println("Repeating the current song");
    } else if (playMode.equals("Repeat All Songs")) {
      SongIndex = (SongIndex + 1) % queue.size();
    } else if (playMode.equals("Shuffle the playlist")) {
      if (queue.size() > 1) {
        int newIndex = SongIndex;
        while (newIndex == SongIndex) {
          newIndex = new Random().nextInt(queue.size());
        }
        SongIndex = newIndex;
      }
    }
    NowPlaying();
  }
  public void QueueToFile(String filename ){
    try(ObjectOutputStream outputStream = new ObjectOutputStream(new FileOutputStream(filename))) {
      outputStream.writeObject(queueIndices);
      System.out.println("Queue saved to file: "+ filename);
      
    } catch (Exception e) {
      e.printStackTrace();
    }

  }
   public void QueueFromFile(String filename) {
    try {
      ObjectInputStream inputStream = new ObjectInputStream(new FileInputStream(filename));{
        queueIndices = (List<Integer>) inputStream.readObject();
        System.out.println("Queue loaded from file: " + filename);
      }
    } catch (IOException | ClassNotFoundException e) {
      e.printStackTrace();

        }
 
}

public static class MiniMusicPlayer {
  public static void main(String[] args) {
    Scanner sc = new Scanner(System.in);
    Musicplayer player = new Musicplayer();
    player.QueueToFile("queue.ser");
    player.QueueFromFile("queue.ser");

    while (true) {
      System.out.println("\nMENU:");
      System.out.println("1. Add a Song");
      System.out.println("2. Display the Playlist");
      System.out.println("3. Search a Song");
      System.out.println("4. Create a Queue");
      System.out.println("5. Edit Queue");
      System.out.println("6. Change the Play Mode");
      System.out.println("7. Play Next Song");
      System.out.println("8. Now Playing");
      System.out.println("9. Exit");

      System.out.print("Enter your choice: ");
      String choice = sc.nextLine();

      switch (choice) {
        case "1":
          System.out.print("Enter song Name: ");
          String title = sc.nextLine();
          System.out.print("Name of the artist: ");
          String artist = sc.nextLine();
          System.out.print("Duration of the song: ");
          int duration = Integer.parseInt(sc.nextLine());
          Song song = new Song(title, artist, duration, duration);
          player.addsong(song);
          System.out.println(song + " has Been added to the Playlist");
          break;

        case "2":
          player.ShowPlaylist();
          break;
        case "3":
          System.out.print("Enter keyword to search: ");
          String keyword = sc.nextLine();
          List<Song> matchingSongs = player.SearchSongs(keyword);
          if (!matchingSongs.isEmpty()) {
            System.out.println("Matching songs:");
            int songNumber = 1;
            for (Song matchingSong : matchingSongs) {
              System.out.println(songNumber + ". " + matchingSong);
              songNumber++;
            }
          } else {
            System.out.println("No matching songs found.");
          }
          break;

        case "4":
          System.out.print("Enter keyword to create a queue: ");
          String queueKeyword = sc.nextLine();
          player.QueueOfSong(queueKeyword);
          break;

        case "5":
          System.out.print("Enter 'Add' or 'Remove' to edit the queue: ");
          String action = sc.nextLine();
          System.out.print("Enter song title or song number: ");
          String value = sc.nextLine();
          player.QueueEdit(action, value);
          break;
        case "6":
          System.out.print("Enter 'Repeat One', 'Repeat All', or 'Shuffle': ");
          String mode = sc.nextLine();
          player.ChangeMode(mode);
          break;

        case "7":
          player.NextSong();
          break;

        case "8":
          player.NowPlaying();
          break;

        case "9":
          System.out.println("Exiting");
          sc.close();
          System.exit(0);

        default:
          System.out.println("Invalid choice. Please enter a valid option.");
      }
    }
  }

}
}