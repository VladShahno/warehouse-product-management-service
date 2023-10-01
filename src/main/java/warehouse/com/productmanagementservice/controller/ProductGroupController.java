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
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
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
import warehouse.com.productmanagementservice.mapper.ProductGroupMapper;
import warehouse.com.productmanagementservice.model.dto.request.CreateProductGroupDto;
import warehouse.com.productmanagementservice.model.dto.response.ProductGroupFullResponseDto;
import warehouse.com.productmanagementservice.model.entity.ProductGroup;
import warehouse.com.productmanagementservice.service.ProductGroupService;

@RestController
@AllArgsConstructor
@Validated
@Tag(name = "Product group Controller", description = "Provides general operation with Product group")
@RequestMapping(path = "/v1/product-groups", produces = MediaType.APPLICATION_JSON_VALUE)
public class ProductGroupController {

  private final ProductGroupService productGroupService;
  private final ProductGroupMapper productGroupMapper;

  @GetMapping
  @ResponseStatus(HttpStatus.OK)
  @Operation(summary = "Endpoint allows to get all product groups")
  @ApiResponses(value = {
      @ApiResponse(responseCode = "200", description = "Product groups received successfully",
          content = {@Content(schema = @Schema(implementation = ProductGroup.class))}),
  })
  public Page<ProductGroupFullResponseDto> findAllProductGroups(
      @PageableDefault(size = 25, sort = {
          "productGroupName"
      }) Pageable pageable) {
    return productGroupService.getAll(pageable)
        .map(productGroupMapper::toProductGroupFullResponseDto);
  }

  @DeleteMapping("/{id}")
  @ResponseStatus(HttpStatus.NO_CONTENT)
  @Operation(summary = "Endpoint allows to delete product group")
  @ApiResponses(value = {
      @ApiResponse(responseCode = "204", description = "Successfully deleted product group")
  })
  public void deleteProductGroup(
      @Parameter(description = "Target product group id", example = "1")
      @NotNull(message = ID_IS_REQUIRED) @PathVariable Long id) {
    productGroupService.deleteById(id);
  }

  @GetMapping("/{id}")
  @ResponseStatus(HttpStatus.OK)
  @Operation(summary = "Endpoint allows to get product group by id")
  @ApiResponses(value = {
      @ApiResponse(responseCode = "200", description = "Product group received successfully")
  })
  public ProductGroupFullResponseDto getProductGroupById(
      @Parameter(description = "Target product name", example = "1")
      @NotNull(message = ID_IS_REQUIRED) @PathVariable Long id) {
    return productGroupMapper.toProductGroupFullResponseDto(productGroupService.findById(id));
  }

  @PostMapping
  @ResponseStatus(HttpStatus.CREATED)
  @Operation(summary = "Endpoint allows to create product group")
  @ApiResponses(value = {
      @ApiResponse(responseCode = "201", description = "Successfully created product group")
  })
  public ProductGroupFullResponseDto createProductGroup(
      @RequestBody @Valid CreateProductGroupDto requestDto) {
    return productGroupMapper.toProductGroupFullResponseDto(productGroupService.create(requestDto));
  }

  @PatchMapping("/edit/{id}")
  @ResponseStatus(HttpStatus.OK)
  @Operation(summary = "Endpoint allows to update product group")
  @ApiResponses(value = {
      @ApiResponse(responseCode = "200", description = "Successfully updated product group")
  })
  public ProductGroupFullResponseDto updateProductGroup(
      @Parameter(description = "Target product group name", example = "1")
      @NotNull(message = ID_IS_REQUIRED) @PathVariable Long id,
      @RequestBody @Valid CreateProductGroupDto requestDto) {
    return productGroupMapper.toProductGroupFullResponseDto(
        productGroupService.update(id, requestDto));
  }
}
