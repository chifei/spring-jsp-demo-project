package app.demo.product.web;

import app.demo.common.util.Messages;
import app.demo.common.web.UserPreference;
import app.demo.common.web.interceptor.LoginRequired;
import app.demo.common.web.interceptor.PermissionRequired;
import app.demo.product.domain.Product;
import app.demo.product.service.ProductService;
import app.demo.product.web.product.ProductBean;
import app.demo.product.web.product.ProductQuery;
import com.google.common.collect.Lists;
import com.google.common.io.ByteStreams;
import com.opencsv.CSVWriter;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.util.FileCopyUtils;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import javax.inject.Inject;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.util.List;


@Controller
public class ProductController {

    @Inject
    Messages messages;

    @Inject
    UserPreference userPreference;

    @Inject
    ProductService productService;

    @RequestMapping(value = "/admin/product", method = RequestMethod.GET, produces = "text/html;charset=UTF-8")
    @LoginRequired
    @PermissionRequired("product.read")
    public String productList(ModelMap model) {
        return "/products/list.jsp";
    }

    @RequestMapping(value = "/admin/product/create", method = RequestMethod.GET, produces = "text/html;charset=UTF-8")
    @LoginRequired
    @PermissionRequired("product.read")
    public String productUpdate(ModelMap model) {
        return "/products/create.jsp";
    }


    @RequestMapping(value = "/admin/product/{id}", method = RequestMethod.GET, produces = "text/html;charset=UTF-8")
    @LoginRequired
    @PermissionRequired("product.read")
    public String productUpdate(ModelMap model, @PathVariable("id") String id) {
        Product product = productService.get(id);
        ProductBean response = response(product);
        model.put("product", response);
        return "/products/update.jsp";
    }

    @RequestMapping(value = "/admin/product/{id}/view", method = RequestMethod.GET, produces = "text/html;charset=UTF-8")
    @LoginRequired
    @PermissionRequired("product.read")
    public String view(ModelMap model, @PathVariable("id") String id) {
        Product product = productService.get(id);
        ProductBean response = response(product);
        model.put("product", response);
        return "/products/view.jsp";
    }

    @RequestMapping("/admin/product/template/download")
    @LoginRequired
    @PermissionRequired("product.read")
    public void download(HttpServletResponse response) throws IOException {
        response.setContentType("text/csv");
        response.setHeader("Content-disposition", "attachment; filename=product_template.csv");
        byte[] bytes = ByteStreams.toByteArray(this.getClass().getResourceAsStream("/download/product_template.csv"));
        FileCopyUtils.copy(bytes, response.getOutputStream());
    }

    @RequestMapping("/admin/product/export")
    @LoginRequired
    @PermissionRequired("product.read")
    public void export(HttpServletResponse response, @RequestParam(required = false) String name) throws IOException {
        response.setContentType("text/csv");
        response.setHeader("Content-disposition", "attachment; filename=product.csv");
        CSVWriter csvWriter = new CSVWriter(new OutputStreamWriter(response.getOutputStream()));
        ProductQuery productQuery = new ProductQuery();
        productQuery.name = name;
        productQuery.page = 1;
        productQuery.limit = 1000;
        List<Product> productList = Lists.newArrayList();
        Page<Product> productPage = productService.find(productQuery);
        productList.addAll(productPage.getContent());
        while (productPage.getTotalPages() > productQuery.page) {
            productQuery.page = productQuery.page + 1;
            productPage = productService.find(productQuery);
            productList.addAll(productPage.getContent());
        }
        csvWriter.writeNext(new String[]{"Product Name", "Product Description"});
        for (Product product : productList) {
            csvWriter.writeNext(new String[]{product.name, product.description});
        }
        csvWriter.close();
    }


    private ProductBean response(Product product) {
        ProductBean response = new ProductBean();
        response.id = product.id;
        response.name = product.name;
        response.description = product.description;
        return response;
    }
}
