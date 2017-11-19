package de.dickert.reporisktool.Controller;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import de.dickert.reporisktool.Model.FileTree;
import de.dickert.reporisktool.Model.RepoItem;
import de.dickert.reporisktool.Model.TreeNode;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.File;
import java.lang.reflect.Type;
import java.util.Collections;

@RestController
public class HtmlController
{
    private static final Logger log = LoggerFactory.getLogger(HtmlController.class);

    @Autowired
    @Qualifier("FileTreeControllerImpl")
    private FileTreeController fileTreeController;

    @RequestMapping("/stats")
    public Model stats(Model model)
    {
        final FileTree tree = fileTreeController.buildFileTree(new File("").toPath(),
                Collections.emptyList(), Collections.emptyList());
        model.addAttribute("rootFileTree", tree.getFileTree());
        return model;
    }

    @RequestMapping("/filetree")
    public Model filetree(Model model)
    {
        log.debug("Serving /filetree");
        final TreeNode tree = fileTreeController.buildTree();
        final String treeJson = fileTreeController.getJsonRepresentation(tree);

        model.addAttribute("fileTree", treeJson);

        model.addAttribute("test", "bla value");

        return model;
    }
}
