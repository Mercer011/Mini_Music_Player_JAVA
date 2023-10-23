import java.util.*;


class Song {
  private String title;
  private String artist;
  private int duration;

  public Song(String title, String artist, int duration) {
    this.title = title;
    this.artist = artist;
    this.duration = duration;
  }

  public String toString() {
    return title + " by " + artist + " (" + duration + " seconds)";
  }
}

class Musicplayer {
  private Queue<Song> listofsongs = new LinkedList<>();
  private Queue<Song> queue = new LinkedList<>();
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

public void NowPlaying() {
  Song currentSong = queue.peek();
  if (currentSong != null) {
    System.out.println("Song playing: " + currentSong);
  } else {
    System.out.println("Currently no song is playing.");
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

public void QueueEdit(String action, String value) {
  if (action.equals("ADD")) {
    List<Song> matchingSongs = SearchSongs(value);
    if (matchingSongs.isEmpty()) {
      System.out.println("Songs not found to add to the queue");
        } else {
      queue.addAll(matchingSongs);
      System.out.println("Added songs successfully.");
    }
  } else if (action.equals("REMOVE")) {
    try {
      int songNumber = Integer.parseInt(value);
      if (songNumber >= 0 && songNumber <= queue.size()) {
        Song removedSong = queue.poll();
        System.out.println("Removed: " + removedSong);
      } else {
        System.out.println("Invalid input!");
      }
    } catch (Exception e) {
      System.out.println("Enter a valid Song Number");
    }
  }
}

public void ChangeMode(String mode) {
  mode = mode.toLowerCase();
  if (mode.equals("repeat one") || mode.equals("repeat all") || mode.equals("shuffle")) {
    playMode = mode;
    System.out.println("Play mode set to: " + mode);
  } else {
    System.out.println("Invalid playing mode, please choose between the available modes");
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
}

public class MiniMusicPlayer {
  public static void main(String[] args) {
    Scanner sc = new Scanner(System.in);
    Musicplayer player = new Musicplayer();

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
          Song song = new Song(title, artist, duration);
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
