package isel.pg3.music.views;

import javax.swing.*;
import java.awt.event.ItemEvent;
import java.util.Collection;
import java.util.HashSet;

/**
 * this menu maintains a check list of the selected genres
 */
public class MenuItemCheckList extends JMenu {
     private Collection<String> selected;

     public MenuItemCheckList(String title) {
         super(title);
         selected = new HashSet<>();
     }

     private void itemListener(ItemEvent e) {
         JCheckBoxMenuItem src =
             (JCheckBoxMenuItem) e.getSource();
         if (e.getStateChange() == ItemEvent.DESELECTED) {
             selected.remove(src.getText());
         }
         else {
             selected.add(src.getText());
         }
     }

     public MenuItemCheckList addItem(String text) {
         var mi = new JCheckBoxMenuItem(text);
         mi.addItemListener(this::itemListener);
         add(mi);
         return this;
     }

     public String[] getSelected() {
         return selected.toArray(sz -> new String[sz]);
     }
}
