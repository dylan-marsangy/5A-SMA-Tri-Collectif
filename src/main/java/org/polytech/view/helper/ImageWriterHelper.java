package org.polytech.view.helper;

import javafx.embed.swing.SwingFXUtils;
import javafx.scene.Node;
import javafx.scene.SnapshotParameters;
import javafx.scene.image.WritableImage;
import javafx.stage.Window;
import sun.plugin2.os.windows.Windows;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

/**
 * Classe utilitaire pour la sauvegarde d'images.
 */
public class ImageWriterHelper {

    /**
     * Format de sauvegarde des images.
     */
    private static final String FILE_FORMAT = "jpeg";

    /**
     * Dossier dans lequel sauvegarder les images.
     */
    private static final String FILE_NAME = "extern/snapshots/snapshot." + FILE_FORMAT;

    /**
     * Sauvegarde un node dans le format référencé par {@link #FILE_FORMAT}.
     * @param node Noeud JavaFX à sauvegarder
     * @param owner Fenêtre dans laquelle afficher un éventuel message d'erreur
     */
    public static void save(Node node, Window owner) {
        WritableImage snapshot = node.snapshot(new SnapshotParameters(), null);
        BufferedImage image = SwingFXUtils.fromFXImage(snapshot, null);
        try {
            File output = new File(FILE_NAME);
            if (output.getParentFile().mkdirs() || output.exists()) { // Créer le dossier si nécessaire
                ImageIO.write(image, FILE_FORMAT, output); // Enregistre l'image
            } else {
                AlertHelper.showError(owner,
                        "Sauvegarde du système",
                        String.format("Le chemin d'accès %s n'existe pas, veuillez le créer.", FILE_NAME));
            }
        } catch (SecurityException e) {
            AlertHelper.showError(owner,
                    "Sauvegarde du système",
                    String.format("Il est impossible de vérifier ou de créer le chemin d'accès %s.", FILE_NAME));
        } catch (IOException e) {
            AlertHelper.showError(owner,
                    "Sauvegarde du système",
                    "Il est impossible de sauvegarder l'image. Veuillez réessayer plus tard");
        }
    }
}
