package se.azza.recursivelymachine.service;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.runners.MockitoJUnitRunner;

import java.nio.file.Files;
import java.nio.file.Paths;

@RunWith(MockitoJUnitRunner.class)
public class RecursivelyMachineSaverImplTest {

    String expectedDir = "https://tretton37.com/";
    String expectedHTML = "https://www.google.com/";
    String expectedImg = "https://tretton37.com/assets/i/office-borlange.jpg";
    RecursivelyMachineSaverImpl recursivelyMachineSaver = new RecursivelyMachineSaverImpl();

    @Test
    public void createAndcheckIfDirectoryIsCreated() {
        recursivelyMachineSaver.createDir(expectedDir);
        Assert.assertTrue(expectedDir, Files.isDirectory(Paths.get(expectedDir)));
    }

    @Test
    public void createDirectoryAndSaveHtmlFile() {
        recursivelyMachineSaver.downloadHtml(expectedHTML);
        Assert.assertTrue(expectedHTML, Files.exists(Paths.get(expectedHTML)));
    }

    @Test
    public void createDirectoryAndSaveImgFile() {
        recursivelyMachineSaver.downloadResource(expectedImg);
        Assert.assertTrue(expectedImg, Files.exists(Paths.get(expectedImg)));
    }
}