package leetc.pg3.music.views;


import leetc.pg3.music.model.*;
import leetc.pg3.music.utils.Mp3Player;
import leetc.pg3.music.utils.Utils;

import javax.swing.*;

import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.util.ArrayList;
import java.util.List;


import static leetc.pg3.music.model.MusicItemFinder.*;
import static leetc.pg3.music.utils.Utils.*;
import static leetc.pg3.music.views.ItemsViewer.*;

public class Mp3PlayerFrame extends JFrame {
    private static final String DEFAULT_FOLDER_MUSICS = ".";

    // states for the music player
    private enum State { Playing, Paused, Stopped }

    // current state
    private State state = State.Stopped;

    // Components for the south panel
    private Icon playIcon =  Utils.getIconFromResource("play.png");
    private Icon pauseIcon = Utils.getIconFromResource("pause.png");
    private Icon stopIcon = Utils.getIconFromResource("stop.png");
    private JButton playPauseBut = getButton(playIcon, this::onPlayPause);
    private JTextField albumName = new JTextField(32);
    private JTextField songName  = new JTextField(32);

    // Content panel for viewer
    private ItemsViewer musicItemsViewer = new ItemsViewer();

    // Components for the playList dialog creation - is incompleted
    private JTextField playlistName = new JTextField(20);
    private JTextField playlistDuration = new JTextField("3600", 6);


    // Model

    // This list is used just for this sample
    // In your aplication you must replace this with a MusicDB as shown in the next commented line
    private List<Song> songs = new ArrayList<>();

    //private MusicDB musicDB = new MusicDB();

    private Mp3Player player = new Mp3Player();



    private void showOn(String title, Iterable<?> values,
                        SelectedItemListener selected, ZoomItemListener zoom) {
        musicItemsViewer.setItems(title, values, selected, zoom);
    }

    private  JComponent[] inputsPlayList = {
        new JLabel("Name"), playlistName,
        new JLabel("Duration"), playlistDuration
        // TODO .. is incompleted
    };

    private void initInputsPlayList() {
        playlistName.setText( "" );
        playlistDuration.setText( "3600");
    }

    private int getInputsPlaylist(String title ) {
        initInputsPlayList();
        return JOptionPane.showConfirmDialog(this, inputsPlayList,
            title, JOptionPane.PLAIN_MESSAGE);
    };



    // listeners for viewer
    private void onSelectedAlbumItem( Object obj) {  }
    private void onZoomAlbumItem( Object obj) { }

    private void onSelectedSongItem(Object obj) {
        Song s = (Song) obj;
        songName.setText( s.getTitle() );
        albumName.setText(s.getAlbum());
    }

    private void onZoomArtistsItem( Object obj) {  }
    private void onZoomGenresItem( Object obj) {  }


    // listeners for Player
    private void onCompletedList( ) {
        state = State.Stopped;
        player.stop();
        //TODO - Update text fields
    }

    private void onMusicStartPlay( String  title ) {
        songName.setText(title);
        //TODO -Update text field albumName with album name
    }

    private void onError( Exception ex ) {
        // TODO
    }

    /**
     * listeners for buttons
     * Incomplete methods to exemplify the use of MP3Player
     */
    private void onStop(ActionEvent evt) {
         if (state != State.Stopped) {
            state = State.Stopped;
            player.stop();
            //TODO
        }
    }

    private void onPlayPause(ActionEvent evt) {
        // TODO - is incomplete
        switch(state) {
            case Playing:
                state = State.Paused;
                player.pause();
                break;
            case Stopped:
                MusicItem selectedItem = musicItemsViewer.getSelectedItem();
                if (selectedItem != null ) {
                    state = State.Playing;
                    selectedItem.playOn(player);
                }
                break;
            case Paused:
                state = State.Playing;
                player.resume();
                break;
        }
    }

    /**
     * Incomplete methods to exemplify addition and visualization
     * (use song as example)
     */
    private void addSongItem(ActionEvent e) {
        try {
            //Note that you must define DEFAULT_FOLDER_MUSICS according to your file system
            File  f = chooseFile(DEFAULT_FOLDER_MUSICS);
            if (f != null) {
                Song s = getSong(f);
                songs.add(s);
                showOn("Songs", s , this::onSelectedSongItem, null);
            }
        }
        catch (Exception ex ) {
            JOptionPane.showMessageDialog(this, ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }

    }

    private void showSongItem(ActionEvent e) {
        albumName.setText( "" );
        songName.setText( "" );
        showOn("songs", songs, this::onSelectedSongItem, null);
    }

    private void buildMenus() {
        // Only for test
        JMenuBar menuBar = new JMenuBar();
        JMenu menu = new JMenu("Example");
        JMenuItem mi = new JMenuItem( "Add song ");
        mi.addActionListener(this::addSongItem);
        menu.add( mi );
        mi = new JMenuItem( "show song ");
        mi.addActionListener(this::showSongItem);
        menu.add( mi );
        menuBar.add( menu );
        setJMenuBar( menuBar );
    }

    private void initComponents() {
        JPanel musicPanel = new JPanel(new GridLayout(2,1));
        musicPanel.add(getBox("Album", albumName, playPauseBut));
        musicPanel.add(getBox("Song", songName, getButton(stopIcon, this::onStop)));

        Container pane = getContentPane();
        pane.add(musicItemsViewer);
        pane.add(musicPanel, BorderLayout.SOUTH);
    }

    private void initModel() {
        player.setCompletedListListener (this::onCompletedList);
        player.setStartMusicListener(this::onMusicStartPlay);
    }

    public Mp3PlayerFrame() {
        initModel();
        initComponents();
        buildMenus();
        setSize(750, 400);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
    }
}
