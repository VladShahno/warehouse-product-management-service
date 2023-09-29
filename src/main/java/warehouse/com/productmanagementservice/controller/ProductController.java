package warehouse.com.productmanagementservice.controller;

import static warehouse.com.productmanagementservice.common.Constants.ProductManagementValidation.ID_IS_REQUIRED;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import warehouse.com.productmanagementservice.mapper.ProductMapper;
import warehouse.com.productmanagementservice.model.ProductGroup;
import warehouse.com.productmanagementservice.model.dto.request.ProductDto;
import warehouse.com.productmanagementservice.model.dto.response.ProductResponseDto;
import warehouse.com.productmanagementservice.service.ProductService;

@RestController
@AllArgsConstructor
@Validated
@Tag(name = "Product Controller", description = "Provides general operation with Product")
@RequestMapping(path = "/v1/products")
public class ProductController {

  private final ProductService productService;
  private final ProductMapper productMapper;

  @PostMapping
  @ResponseStatus(HttpStatus.CREATED)
  @Operation(summary = "Endpoint allows to create product")
  @ApiResponses(value = {
      @ApiResponse(responseCode = "201", description = "Successfully created product")
  })
  public ProductResponseDto createProduct(@RequestBody @Valid ProductDto productDto) {
    return productMapper.toDto(productService.create(productDto));
  }

  @GetMapping("/{id}")
  @ResponseStatus(HttpStatus.OK)
  @Operation(summary = "Endpoint allows to get product by id")
  @ApiResponses(value = {
      @ApiResponse(responseCode = "200", description = "Product received successfully")
  })
  public ProductResponseDto getProductById(
      @Parameter(description = "Target product id", example = "5")
      @NotBlank(message = ID_IS_REQUIRED) @PathVariable Long id) {
    return productMapper.toDto(productService.findById(id));
  }

  @PatchMapping("/edit/{id}")
  @ResponseStatus(HttpStatus.OK)
  @Operation(summary = "Endpoint allows to edit product")
  @ApiResponses(value = {
      @ApiResponse(responseCode = "200", description = "Successfully updated product")
  })
  public ProductResponseDto updateProduct(
      @Parameter(description = "Target product id", example = "6")
      @NotBlank(message = ID_IS_REQUIRED) @PathVariable Long id,
      @RequestBody @Valid ProductDto requestDto) {
    return productMapper.toDto(productService.update(id, requestDto));
  }

  @DeleteMapping("/{id}")
  @ResponseStatus(HttpStatus.NO_CONTENT)
  @Operation(summary = "Endpoint allows to delete product")
  @ApiResponses(value = {
      @ApiResponse(responseCode = "204", description = "Successfully deleted product")
  })
  public void deleteProduct(
      @Parameter(description = "Target product id", example = "5")
      @NotBlank(message = ID_IS_REQUIRED) @PathVariable Long id) {
    productService.deleteById(id);
  }

  @GetMapping
  @ResponseStatus(HttpStatus.OK)
  @Operation(summary = "Endpoint allows to get all products")
  @ApiResponses(value = {
      @ApiResponse(responseCode = "200", description = "Products received successfully",
          content = {@Content(schema = @Schema(implementation = ProductGroup.class))}),
  })
  public Page<ProductResponseDto> findAllProducts(
      @PageableDefault(size = 25, sort = {"productName"}) Pageable pageable) {
    return productService.getAll(pageable).map(productMapper::toDto);
  }
}
