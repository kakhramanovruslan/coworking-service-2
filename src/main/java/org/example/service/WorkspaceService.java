package org.example.service;

import lombok.RequiredArgsConstructor;
import org.example.dao.WorkspaceDao;
import org.example.entity.Workspace;
import org.example.exceptions.WorkspaceAlreadyExist;
import org.example.exceptions.WorkspaceNotFoundException;

import java.util.List;
import java.util.Optional;

/**
 * Service class for managing workspaces.
 */
@RequiredArgsConstructor
public class WorkspaceService {

    private final WorkspaceDao workspaceDao;

    /**
     * Retrieves a list of all workspaces.
     * @return List of all workspaces
     */
    public List<Workspace> getListOfAllWorkSpaces() {
        return workspaceDao.findAll();
    }

    /**
     * Creates a new workspace with the given name.
     * @param workspace new Workspace
     * @return The created workspace object
     */
    public Workspace createWorkspace(Workspace workspace) throws WorkspaceAlreadyExist{
        Optional<Workspace> w = workspaceDao.findByName(workspace.getName());
        if (w.isPresent()) throw new WorkspaceAlreadyExist("Workspace с таким имененм уже существует");
        return workspaceDao.save(workspace);
    }

    /**
     * Deletes a workspace by its name.
     * @param name Name of the workspace to delete
     * @return True if deletion was successful, false otherwise
     */
    public boolean deleteWorkspace(String name) throws WorkspaceNotFoundException{
        Workspace workspace = this.getWorkspace(name);
        return workspaceDao.deleteByName(workspace.getName());
    }

    /**
     * Deletes a workspace by its ID.
     * @param id ID of the workspace to delete
     * @return True if deletion was successful, false otherwise
     */
    public boolean deleteWorkspace(Long id) {
        Workspace workspace = this.getWorkspace(id);
        return workspaceDao.deleteById(workspace.getId());
    }

    /**
     * Updates the name of a workspace.
     * @param oldName Current name of the workspace
     * @param workspace updated Workspace
     * @return True if update was successful, false otherwise
     */
    public boolean updateWorkspace(String oldName, Workspace workspace) throws WorkspaceNotFoundException, WorkspaceAlreadyExist{
        Workspace oldWorkspace = this.getWorkspace(oldName);

        Optional<Workspace> newWorkspace = this.getWorkspaceByName(workspace.getName());
        if (newWorkspace.isPresent()) throw new WorkspaceAlreadyExist("Workspace с таким именем уже существует");

        workspace.setId(oldWorkspace.getId());
        return workspaceDao.update(workspace);
    }

    /**
     * Retrieves a workspace by its name.
     * @param name Name of the workspace to retrieve
     * @return Optional containing the workspace if found, otherwise empty
     */
    public Optional<Workspace> getWorkspaceByName(String name) {
        return workspaceDao.findByName(name);
    }

    /**
     * Retrieves a workspace by its ID.
     * @param id ID of the workspace to retrieve
     * @return Optional containing the workspace if found, otherwise empty
     */
    public Optional<Workspace> getWorkspaceById(Long id) {
        return workspaceDao.findById(id);
    }

    private Workspace getWorkspace(String name) throws WorkspaceNotFoundException{
        return workspaceDao.findByName(name)
                .orElseThrow(() -> new WorkspaceNotFoundException("Workspace с таким именем не существует"));
    }

    private Workspace getWorkspace(Long id) throws WorkspaceNotFoundException{
        return workspaceDao.findById(id)
                .orElseThrow(() -> new WorkspaceNotFoundException("Workspace с таким именем не существует"));
    }

}
