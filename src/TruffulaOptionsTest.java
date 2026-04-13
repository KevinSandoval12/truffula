import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.File;
import java.io.FileNotFoundException;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

public class TruffulaOptionsTest {

  @Test
  void testValidDirectoryIsSet(@TempDir File tempDir) throws FileNotFoundException {
    // Arrange: Prepare the arguments with the temp directory
    File directory = new File(tempDir, "subfolder");
    directory.mkdir();
    String directoryPath = directory.getAbsolutePath();
    String[] args = {"-nc", "-h", directoryPath};

    // Act: Create TruffulaOptions instance
    TruffulaOptions options = new TruffulaOptions(args);

    // Assert: Check that the root directory is set correctly
    assertEquals(directory.getAbsolutePath(), options.getRoot().getAbsolutePath());
    assertTrue(options.isShowHidden());
    assertFalse(options.isUseColor());
  }

  @Test
  void testOnlyPathDefaults(@TempDir File tempDir) throws FileNotFoundException {
    File directory = new File(tempDir, "subfolder");
    directory.mkdir();
    String[] args = {directory.getAbsolutePath()};

    TruffulaOptions options = new TruffulaOptions(args);

    assertEquals(directory.getAbsolutePath(), options.getRoot().getAbsolutePath());
    assertFalse(options.isShowHidden()); 
    assertTrue(options.isUseColor());    
  }

  @Test
  void testShowHiddenFlag(@TempDir File tempDir) throws FileNotFoundException {
    File directory = new File(tempDir, "subfolder");
    directory.mkdir();
    String directoryPath = directory.getAbsolutePath();
    String[] args = {"-h", directoryPath};
    TruffulaOptions options = new TruffulaOptions(args);

    assertTrue(options.isShowHidden());
    assertTrue(options.isUseColor()); 
  }

  @Test
  void testNoColorFlag(@TempDir File tempDir) throws FileNotFoundException {
    File directory = new File(tempDir, "subfolder");
    directory.mkdir();
    String[] args = {"-nc", directory.getAbsolutePath()};

    TruffulaOptions options = new TruffulaOptions(args);

    assertFalse(options.isUseColor());
    assertFalse(options.isShowHidden()); 
  }

  @Test
  void testEmptyArgsThrows() {
    String[] args = {};

    assertThrows(IllegalArgumentException.class, () -> {
      new TruffulaOptions(args);
    });
  }

  @Test
  void testMissingPathThrows() {
    String[] args = {"-h"};

    assertThrows(IllegalArgumentException.class, () -> {
      new TruffulaOptions(args);
    });
  }

  @Test
  void testUnknownFlagThrows(@TempDir File tempDir) {
    File directory = new File(tempDir, "subfolder");
    directory.mkdir();
    String[] args = {"-x", directory.getAbsolutePath()};

    assertThrows(IllegalArgumentException.class, () -> {
      new TruffulaOptions(args);
    });
  }

  @Test
  void testInvalidArgumentThrows(@TempDir File tempDir) {
    File directory = new File(tempDir, "subfolder");
    directory.mkdir();
    String[] args = {"abc", directory.getAbsolutePath()};

    assertThrows(IllegalArgumentException.class, () -> {
      new TruffulaOptions(args);
    });
  }

  @Test
  void testNonexistentPathThrows() {
    String[] args = {"/fake/path/that/does/not/exist"};

    assertThrows(FileNotFoundException.class, () -> {
      new TruffulaOptions(args);
    });
  }

  @Test
  void testPathIsFileThrows(@TempDir File tempDir) throws Exception {
    File file = new File(tempDir, "file.txt");
    file.createNewFile(); 

    String[] args = {file.getAbsolutePath()};

    assertThrows(FileNotFoundException.class, () -> {
      new TruffulaOptions(args);
    });
  }

}
