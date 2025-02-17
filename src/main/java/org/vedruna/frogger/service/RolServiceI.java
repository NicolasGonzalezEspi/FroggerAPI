package org.vedruna.frogger.service;

import org.vedruna.frogger.dto.RolDTO;

public interface RolServiceI {
    RolDTO selectRolByName(String rolName);
}
