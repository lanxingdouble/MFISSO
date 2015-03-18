package cn.fdse.StackOverflow.searchModule.util;

import java.util.List;

import cn.fdse.StackOverflow.searchModule.Post;
import cn.fdse.codeSearch.openInterface.searchResult.CodeResult;

public interface PostDAO {
	public List<Post> findPosts() throws Exception;
	public List<CodeResult> findPosts(String keyword) throws Exception;
}
