package utils;

import java.awt.Component;
import java.awt.Container;
import java.util.Locale;
import java.util.ResourceBundle;

import javax.swing.AbstractButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;
import javax.swing.text.JTextComponent;

public class I18nUtils {

    private static ResourceBundle bundle;

    /**
     * Cambia el idioma de la aplicación.
     *
     * @param language Código ISO (ej: "es", "en", "fr")
     */
    public static void setLanguage(String language) {
        switch (language) {
            case "en_US":
                bundle = ResourceBundle.getBundle("resources.Bundle_en_US");
                break;
            case "fr_FR":
                bundle = ResourceBundle.getBundle("resources.Bundle_fr_FR");
                break;
            default:
                bundle = ResourceBundle.getBundle("resources.Bundle");
                break;
        }
    }

    /**
     * Obtiene el texto traducido.
     *
     * @param key Clave del archivo .properties
     * @return Texto traducido
     */
    public static String t(String key) {
        try {
            return bundle.getString(key);
        } catch (Exception e) {
            return "??" + key + "??"; // útil para depurar claves faltantes
        }
    }

    /**
     * Localiza todos los componentes dentro de 'root' usando claves con formato:
     * base + "." + component.getName() + ".text"
     * - Para JFrame usa base + ".title"
     * - Para JTabbedPane usa base + "." + tabComponent.getName() +
     * ".TabConstraints.tabTitle"
     */
    public static void localizeContainer(Component root, String base) {
        if (root == null)
            return;

        // JFrame title
        if (root instanceof JFrame) {
            String titleKey = base + ".title";
            ((JFrame) root).setTitle(t(titleKey));
        }

        // Accessible context
        try {
            String an = t(base + ".AccessibleContext.accessibleName");
            String ad = t(base + ".AccessibleContext.accessibleDescription");
            if (an != null && !an.startsWith("??"))
                root.getAccessibleContext().setAccessibleName(an);
            if (ad != null && !ad.startsWith("??"))
                root.getAccessibleContext().setAccessibleDescription(ad);
        } catch (Exception ignore) {

        }

        // JTabbedPane special handling
        if (root instanceof JTabbedPane) {
            JTabbedPane tp = (JTabbedPane) root;
            for (int i = 0; i < tp.getTabCount(); i++) {
                Component c = tp.getComponentAt(i);
                String compName = (c != null) ? c.getName() : null;
                if (compName != null) {
                    String tabKey = base + "." + compName + ".TabConstraints.tabTitle";
                    tp.setTitleAt(i, t(tabKey));
                    localizeContainer(c, base);
                }
            }
        }

        if (root instanceof Container) {
            for (Component child : ((Container) root).getComponents()) {
                if (child == null)
                    continue;

                String name = child.getName();
                if (name != null && !name.isEmpty()) {
                    String keyBase = base + "." + name;

                    if (child instanceof JLabel) {
                        ((JLabel) child).setText(t(keyBase + ".text"));
                    } else if (child instanceof AbstractButton) {
                        ((AbstractButton) child).setText(t(keyBase + ".text"));
                    } else if (child instanceof JTextComponent) {
                        // placeholder or tooltip
                        String t = t(keyBase + ".text");
                        if (!t.startsWith("??"))
                            ((JTextComponent) child).setText(t);
                    } else if (child instanceof JComboBox) {
                        // Leave items management to controller (don't overwrite)
                        String tooltip = t(keyBase + ".toolTipText");
                        if (!tooltip.startsWith("??"))
                            ((JComboBox<?>) child).setToolTipText(tooltip);
                    } else if (child instanceof JCheckBox) {
                        String text = t(keyBase + ".text");
                        if (!text.startsWith("??"))
                            ((JCheckBox) child).setText(text);

                        String tooltip = t(keyBase + ".toolTipText");
                        if (!tooltip.startsWith("??"))
                            ((JCheckBox) child).setToolTipText(tooltip);
                    } else if (child instanceof JTabbedPane) {
                        // handled above for top-level tabbedpanes; also recurse
                        localizeContainer(child, base);
                    } else if (child instanceof JPanel) {
                        // panels generally don't have visible text; continue recursion
                    }
                }

                // recurse into child
                localizeContainer(child, base);
            }
        }
    }
}
