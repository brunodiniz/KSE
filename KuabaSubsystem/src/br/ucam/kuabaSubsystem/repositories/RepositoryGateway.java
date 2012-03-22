package br.ucam.kuabaSubsystem.repositories;

import java.io.File;

public interface RepositoryGateway {
	public KuabaRepository load(String url) throws RepositoryLoadException;
        public KuabaRepository createNewRepository();
	public KuabaRepository createNewRepository(String url);
        public KuabaRepository createNewRepository(File destination);
        public KuabaRepository createNewRepository(String url, File destination);
	public boolean save(KuabaRepository kr);
        public boolean save(KuabaRepository kr, File destination);
        public boolean save(KuabaRepository kr, File destination, String newBaseUrl);
}
