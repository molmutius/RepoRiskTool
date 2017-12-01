package de.dickert.reporisktool.Controller;

import de.dickert.reporisktool.Model.FileTree;
import de.dickert.reporisktool.Model.TreeNode;
import de.dickert.reporisktool.Service.FileTreeService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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
    private static final Logger log = LoggerFactory.getLogger(HtmlController.class);

    @Autowired
    @Qualifier("FileTreeServiceImpl")
    private FileTreeService fileTreeService;

    @RequestMapping("/stats")
    public Model stats(Model model)
    {
        final FileTree tree = fileTreeService.buildFileTree(new File("").toPath(),
                Collections.emptyList(), Collections.emptyList());
        model.addAttribute("rootFileTree", tree.getFileTree());
        return model;
    }

    @RequestMapping("/filetree")
    public Model filetree(Model model)
    {
        log.debug("Serving /filetree");
        final TreeNode tree = fileTreeService.buildTree();
        final String treeJson = fileTreeService.getJsonRepresentation(tree);


        model.addAttribute("fileTree", treeJson);

        return model;
    }
}
