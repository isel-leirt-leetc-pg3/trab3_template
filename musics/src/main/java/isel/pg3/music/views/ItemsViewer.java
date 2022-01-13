package isel.pg3.music.views;


import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

/**
 * This viewer presents the list of the music items
 * of a certain type (albums, songs and playlists)
 * contained in the music database
 */
public class ItemsViewer extends JPanel {

    private final JTable itemsPanel;
    private SelectedItemListener selectedListener;
    private ZoomItemListener zoomListener;

    public interface SelectedItemListener  {
        void onSelectedItem(Object item);
    }
    public interface ZoomItemListener  {
        void onZoomItem(Object item);
    }

    private void fireZoomItem(Object item) {
        if (zoomListener != null)
            zoomListener.onZoomItem(item);
    }

    private  void fireSelectedItem(Object item) {
        if (selectedListener != null)
            selectedListener.onSelectedItem(item);
    }

    public ItemsViewer() {
        itemsPanel = new JTable(new DefaultTableModel( new String[] {"list of ..."}, 0)) {
            @Override
            public boolean isCellEditable(int rowIndex, int columnIndex) { return false; }
        };

        final MouseListener mouse = new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                Object selected = getSelectedItem();
                if (selected == null ) return;
                if (SwingUtilities.isRightMouseButton(e))
                    fireZoomItem( selected );
                else
                    fireSelectedItem( selected );
            }
        };

        itemsPanel.addMouseListener( mouse );
        setRowHeight(25);
        Font f = itemsPanel.getFont().deriveFont(14.0F);
        setFont( f.deriveFont(Font.BOLD), f );
        itemsPanel.setBackground( getBackground() );
        JScrollPane scroller = new JScrollPane(itemsPanel);
        setLayout(new BorderLayout());
        add(scroller, BorderLayout.CENTER);
    }

    public void setRowHeight( int height ) {
        itemsPanel.setRowHeight( height );
    }

    public void setFont( Font fHeader, Font fRow ) {
        itemsPanel.setFont( fRow );
        itemsPanel.getTableHeader().setFont( fHeader);
     }

    public <E> void setItems(String title, Iterable<E> items,
                         SelectedItemListener selectedListener,
                         ZoomItemListener zoomListener) {
        this.selectedListener = selectedListener != null ? selectedListener : i->{};
        this.zoomListener = zoomListener != null ? zoomListener : i->{};
        DefaultTableModel tableModel = (DefaultTableModel) itemsPanel.getModel();
        tableModel.setColumnCount(0);
        tableModel.addColumn(title);
        tableModel.setRowCount( 0 );
        for (E item: items ) {
            tableModel.addRow( new Object[]{ item } );
        }
     }

    @SuppressWarnings("unchecked")
    public <E> E getSelectedItem() {
        int row = itemsPanel.getSelectedRow();
        if ( row != -1 )
            return (E) itemsPanel.getModel().getValueAt(row, 0);
        return null;
    }
}
