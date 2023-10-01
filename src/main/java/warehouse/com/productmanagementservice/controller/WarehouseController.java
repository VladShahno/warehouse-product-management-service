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
import warehouse.com.productmanagementservice.mapper.WarehouseMapper;
import warehouse.com.productmanagementservice.model.dto.request.WarehouseDto;
import warehouse.com.productmanagementservice.model.dto.response.WarehouseResponseDto;
import warehouse.com.productmanagementservice.model.entity.Warehouse;
import warehouse.com.productmanagementservice.service.WarehouseService;

@RestController
@AllArgsConstructor
@Validated
@Tag(name = "Warehouse Controller", description = "Provides general operation with warehouse")
@RequestMapping(path = "/v1/warehouses", produces = MediaType.APPLICATION_JSON_VALUE)
public class WarehouseController {

  private final WarehouseService warehouseService;
  private final WarehouseMapper warehouseMapper;

  @PostMapping
  @ResponseStatus(HttpStatus.CREATED)
  @Operation(summary = "Endpoint allows to create warehouse")
  @ApiResponses(value = {
      @ApiResponse(responseCode = "201", description = "Successfully created warehouse")
  })
  public WarehouseResponseDto createWarehouse(@RequestBody @Valid WarehouseDto requestDto) {
    return warehouseMapper.toWarehouseResponseDto(warehouseService.create(requestDto));
  }

  @GetMapping
  @ResponseStatus(HttpStatus.OK)
  @Operation(summary = "Endpoint allows to get all warehouses")
  @ApiResponses(value = {
      @ApiResponse(responseCode = "200", description = "Warehouses received successfully",
          content = {@Content(schema = @Schema(implementation = Warehouse.class))}),
  })
  public Page<WarehouseResponseDto> findAllWarehouse(
      @PageableDefault(size = 25, sort = {"warehouseName"}) Pageable pageable) {
    return warehouseService.getAll(pageable).map(warehouseMapper::toWarehouseResponseDto);
  }

  @PatchMapping("/edit/{id}")
  @ResponseStatus(HttpStatus.OK)
  @Operation(summary = "Endpoint allows to update warehouse")
  @ApiResponses(value = {
      @ApiResponse(responseCode = "200", description = "Successfully updated warehouse")
  })
  public WarehouseResponseDto updateWarehouse(
      @Parameter(description = "Target warehouse id", example = "1")
      @NotBlank(message = ID_IS_REQUIRED) @PathVariable Long id,
      @RequestBody @Valid WarehouseDto requestDto) {
    return warehouseMapper.toWarehouseResponseDto(warehouseService.update(id, requestDto));
  }

  @DeleteMapping("/{id}")
  @ResponseStatus(HttpStatus.NO_CONTENT)
  @Operation(summary = "Endpoint allows to delete warehouse")
  @ApiResponses(value = {
      @ApiResponse(responseCode = "204", description = "Successfully deleted warehouse")
  })
  public void deleteWarehouseById(
      @Parameter(description = "Target warehouse id", example = "1")
      @NotBlank(message = ID_IS_REQUIRED) @PathVariable Long id) {
    warehouseService.deleteById(id);
  }

  @GetMapping("/{id}")
  @ResponseStatus(HttpStatus.OK)
  @Operation(summary = "Endpoint allows to get warehouse by id")
  @ApiResponses(value = {
      @ApiResponse(responseCode = "200", description = "Warehouse group received successfully")
  })
  public WarehouseResponseDto getWarehouseById(
      @Parameter(description = "Target product id", example = "1")
      @NotBlank(message = ID_IS_REQUIRED) @PathVariable Long id) {
    return warehouseMapper.toWarehouseResponseDto(warehouseService.findById(id));
  }
}
