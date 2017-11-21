package base.config;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import javax.servlet.http.HttpServletRequest;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.util.StringUtils;
import org.springframework.web.servlet.resource.ResourceResolver;
import org.springframework.web.servlet.resource.ResourceResolverChain;

/**
 * package private on purpose
 *
 */
class StaticPathResolver implements ResourceResolver {

    private Resource indexHtml = new ClassPathResource("/templates/index.html");
    private List<String> handledExtensions = Arrays.asList("css", "html", "ico", "js", "map", "woff2");
    private List<String> ignoredPaths = Arrays.asList("api");

    /**
     * Resolve the supplied request and request path to a Resource that exists
     * under one of the given resource locations. request - the current request
     * requestPath - the portion of the request path to use locations - the
     * locations to search in when looking up resources chain - the chain of
     * remaining resolvers to delegate to
     */
    @Override
    public Resource resolveResource(HttpServletRequest request, String requestPath, List<? extends Resource> locations, ResourceResolverChain chain) {
        Resource temp = resolve(requestPath, locations);
        return temp;
    }

    /**
     * Resolve the externally facing public URL path for clients to use to
     * access the resource that is located at the given internal resource path.
     * This is useful when rendering URL links to clients.
     *
     * @param resourcePath
     * @param locations
     * @param chain
     * @return
     */
    @Override
    public String resolveUrlPath(String resourcePath, List<? extends Resource> locations, ResourceResolverChain chain) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    private Resource resolve(String requestPath, List<? extends Resource> locations) {
        if (isIgnored(requestPath)) {
            return null;
        }
        if (isHandled(requestPath)) {
            return locations.stream()
                    .map(loc -> createRelative(loc, requestPath))
                    .filter(resource -> resource != null && resource.exists())
                    .findFirst()
                    .orElseGet(null);
        }
        return indexHtml;
    }

    private Resource createRelative(Resource resource, String relativePath) {
        try {
            Resource relative = resource.createRelative(relativePath);
            return relative;
        } catch (IOException e) {
            return null;
        }
    }

    private boolean isIgnored(String path) {
        return ignoredPaths.contains(path);
        }

    private boolean isHandled(String path) {
        String extension = StringUtils.getFilenameExtension(path);
        return handledExtensions.stream().anyMatch(ext -> ext.equals(extension));
    }

}
