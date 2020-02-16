package org.polytech.view.helper;

import javafx.embed.swing.SwingFXUtils;
import javafx.scene.Node;
import javafx.scene.SnapshotParameters;
import javafx.scene.control.Alert;
import javafx.scene.image.WritableImage;
import javafx.stage.Window;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.function.Function;

/**
 * Classe utilitaire pour la sauvegarde d'images.
 */
public class ImageWriterHelper {

    /**
     * Dossier de sauvegarde des images.
     */
    private static final String FOLDER = "extern/snapshots";

    /**
     * Préfixe attaché à chaque fichier généré.
     */
    private static final String FILE_NAME_PREFIX = "snapshot";

    /**
     * Format de sauvegarde des images.
     */
    private static final String FILE_FORMAT = "png";

    /**
     * Formatteur de date, notamment utilisé comme nom de fichier à générer lors de la sauvegarde d'une image.
     */
    private static final DateFormat DATE_FORMAT = new SimpleDateFormat("dd-MM@HH-mm-ss");

    /**
     * Sauvegarde un node dans le format référencé par {@link #FILE_FORMAT}.
     * @param node Noeud JavaFX à sauvegarder
     * @param owner Fenêtre dans laquelle afficher un éventuel message d'erreur
     */
    public static void save(Node node, Window owner) {
       WritableImage snapshot = node.snapshot(new SnapshotParameters(), null);
        BufferedImage image = SwingFXUtils.fromFXImage(snapshot, null);
        try {
            String fileName = generateFileName();
            File output = new File(fileName);

            if (output.getParentFile().mkdirs() || output.createNewFile() || output.exists()) { // Créer le dossier si nécessaire
                ImageIO.write(image, FILE_FORMAT, output); // Enregistre l'image
            } else {
                AlertHelper.showError(owner,
                        "Sauvegarde du système",
                        String.format("Le chemin d'accès %s n'existe pas, veuillez le créer.", FOLDER));
            }

            AlertHelper.showAlert(Alert.AlertType.INFORMATION, owner,
                    "Sauvegarde du système",
                    "Confirmation de sauvegarde",
                    String.format("La sauvegarde est disponible dans le chemin d'accès suivant : %s", fileName));
        } catch (SecurityException e) {
            AlertHelper.showError(owner,
                    "Sauvegarde du système",
                    String.format("Il est impossible de vérifier ou de créer le chemin d'accès %s.", FOLDER));
        } catch (IOException e) {
            AlertHelper.showError(owner,
                    "Sauvegarde du système",
                    "Il est impossible de sauvegarder l'image. Veuillez réessayer plus tard");
        }
    }

    /**
     * Génère un nom de fichier de sauvegarde d'image.
     * Ce nom contient notamment la timestamp de création du fichier.
     *
     * @return Nom de fichier.
     */
    private static String generateFileName() {
        return String.format("%s/%s-%s.%s", FOLDER, FILE_NAME_PREFIX, DATE_FORMAT.format(new Date()), FILE_FORMAT);
    }

}
