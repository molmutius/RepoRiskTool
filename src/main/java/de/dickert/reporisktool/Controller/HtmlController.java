package de.dickert.reporisktool.Controller;

import de.dickert.reporisktool.Model.*;
import de.dickert.reporisktool.Util.NodeVisitor;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.swing.tree.DefaultMutableTreeNode;
import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

@RestController
public class HtmlController
{
    @Autowired
    @Qualifier("DummyFileTreeControllerImpl")
    FileTreeController fileTreeController;

    private int lastLevel = 0;

    @RequestMapping("/stats")
    public Model stats(Model model)
    {
        final FileTree tree = fileTreeController.buildFileTree(new File("").toPath(),
                Collections.emptyList(), Collections.emptyList());
        model.addAttribute("rootFileTree", tree.getFileTree());
        return model;
    }
}
