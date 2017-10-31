package de.dickert.reporisktool.Controller;

import de.dickert.reporisktool.Model.FileTree;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.File;
import java.util.Collections;

@RestController
public class HtmlController
{
    @Autowired
    @Qualifier("DummyFileTreeControllerImpl")
    FileTreeController fileTreeController;

    @RequestMapping("/stats")
    public Model stats(Model model)
    {
        final FileTree tree = fileTreeController.buildFileTree(new File("").toPath(),
                Collections.emptyList(), Collections.emptyList());
        model.addAttribute("rootFileTree", tree.getFileTree());
        return model;
    }

}
