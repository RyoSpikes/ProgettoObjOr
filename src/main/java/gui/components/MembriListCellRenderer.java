package gui.components;

import javax.swing.*;
import java.awt.*;

/**
 * Renderer personalizzato per la lista dei membri del team.
 */
public class MembriListCellRenderer extends DefaultListCellRenderer {
    
    @Override
    public Component getListCellRendererComponent(JList<?> list, Object value, int index, 
                                                boolean isSelected, boolean cellHasFocus) {
        super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
        
        // Colori personalizzati con gradiente
        if (isSelected) {
            setBackground(new Color(70, 130, 180));
            setForeground(Color.WHITE);
        } else {
            // Alterna i colori per migliorare la leggibilità
            if (index % 2 == 0) {
                setBackground(new Color(248, 255, 248));
            } else {
                setBackground(new Color(240, 250, 240));
            }
            setForeground(new Color(25, 25, 112));
        }
        
        // Font personalizzato
        setFont(new Font("Segoe UI", Font.PLAIN, 13));
        setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createMatteBorder(0, 0, 1, 0, new Color(220, 220, 220)),
            BorderFactory.createEmptyBorder(8, 15, 8, 15)
        ));
        
        // Aggiungi icona in base al contenuto
        String text = value.toString();
        if (text.contains("Errore") || text.contains("Nessun")) {
            setIcon(new ColorIcon(Color.ORANGE, 8));
        } else {
            setIcon(new ColorIcon(new Color(34, 139, 34), 8));
        }
        
        return this;
    }
    
    /**
     * Icona colorata semplice per identificare lo stato.
     */
    private static class ColorIcon implements Icon {
        private Color color;
        private int size;
        
        public ColorIcon(Color color, int size) {
            this.color = color;
            this.size = size;
        }
        
        @Override
        public void paintIcon(Component c, Graphics g, int x, int y) {
            Graphics2D g2d = (Graphics2D) g;
            g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            g2d.setColor(color);
            g2d.fillOval(x, y, size, size);
            g2d.setColor(color.darker());
            g2d.drawOval(x, y, size, size);
        }
        
        @Override
        public int getIconWidth() {
            return size;
        }
        
        @Override
        public int getIconHeight() {
            return size;
        }
    }
}
