package gui.components;

import javax.swing.*;
import java.awt.*;

/**
 * Utility per creare pulsanti con stile moderno e gradienti.
 */
public class ModernButton extends JButton {
    private Color backgroundColor;
    private Color hoverColor;
    private Color pressedColor;
    
    /**
     * Costruttore di default per compatibilità con i file .form
     */
    public ModernButton() {
        this("Button", new Color(70, 130, 180)); // Colore blu di default
    }
    
    public ModernButton(String text, Color backgroundColor) {
        super(text);
        this.backgroundColor = backgroundColor;
        this.hoverColor = backgroundColor.brighter();
        this.pressedColor = backgroundColor.darker();
        
        setupButton();
    }
    
    public ModernButton(String text, Color backgroundColor, Color foregroundColor) {
        super(text);
        this.backgroundColor = backgroundColor;
        this.hoverColor = backgroundColor.brighter();
        this.pressedColor = backgroundColor.darker();
        
        setForeground(foregroundColor);
        setupButton();
    }
    
    private void setupButton() {
        setFont(new Font("Segoe UI", Font.BOLD, 12));
        setFocusPainted(false);
        setBorderPainted(false);
        setContentAreaFilled(false);
        setOpaque(false);
        
        // Dimensioni predefinite
        setPreferredSize(new Dimension(140, 35));
        
        // Cursore pointer
        setCursor(new Cursor(Cursor.HAND_CURSOR));
    }
    
    @Override
    protected void paintComponent(Graphics g) {
        Graphics2D g2d = (Graphics2D) g.create();
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2d.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
        
        int width = getWidth();
        int height = getHeight();
        
        // Determina il colore in base allo stato
        Color currentColor;
        if (getModel().isPressed()) {
            currentColor = pressedColor;
        } else if (getModel().isRollover()) {
            currentColor = hoverColor;
        } else {
            currentColor = backgroundColor;
        }
        
        // Crea gradiente
        GradientPaint gradient = new GradientPaint(
            0, 0, currentColor.brighter(),
            0, height, currentColor.darker()
        );
        g2d.setPaint(gradient);
        
        // Disegna il pulsante con angoli arrotondati
        g2d.fillRoundRect(0, 0, width, height, 12, 12);
        
        // Aggiungi bordo sottile
        g2d.setColor(currentColor.darker().darker());
        g2d.setStroke(new BasicStroke(1f));
        g2d.drawRoundRect(0, 0, width - 1, height - 1, 12, 12);
        
        // Aggiungi effetto luce nella parte superiore
        if (!getModel().isPressed()) {
            g2d.setColor(new Color(255, 255, 255, 40));
            g2d.fillRoundRect(1, 1, width - 2, height / 2, 10, 10);
        }
        
        g2d.dispose();
        super.paintComponent(g);
    }
    
    /**
     * Imposta i colori del pulsante.
     */
    public void setButtonColors(Color backgroundColor, Color hoverColor, Color pressedColor) {
        this.backgroundColor = backgroundColor;
        this.hoverColor = hoverColor;
        this.pressedColor = pressedColor;
        repaint();
    }
    
    /**
     * Crea un pulsante primario (blu).
     */
    public static ModernButton createPrimaryButton(String text) {
        return new ModernButton(text, new Color(70, 130, 180), Color.WHITE);
    }
    
    /**
     * Crea un pulsante di successo (verde).
     */
    public static ModernButton createSuccessButton(String text) {
        return new ModernButton(text, new Color(34, 139, 34), Color.WHITE);
    }
    
    /**
     * Crea un pulsante di pericolo (rosso).
     */
    public static ModernButton createDangerButton(String text) {
        return new ModernButton(text, new Color(220, 20, 60), Color.WHITE);
    }
    
    /**
     * Crea un pulsante di avvertimento (arancione).
     */
    public static ModernButton createWarningButton(String text) {
        return new ModernButton(text, new Color(255, 140, 0), Color.WHITE);
    }
    
    /**
     * Crea un pulsante speciale (oro).
     */
    public static ModernButton createSpecialButton(String text) {
        return new ModernButton(text, new Color(255, 215, 0), new Color(25, 25, 112));
    }
}
