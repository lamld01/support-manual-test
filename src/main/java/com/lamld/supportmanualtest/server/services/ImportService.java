package com.lamld.supportmanualtest.server.services;

import com.lamld.supportmanualtest.app.response.importResponse.ApiInfo;
import com.lamld.supportmanualtest.app.response.importResponse.ImportResponse;
import com.lamld.supportmanualtest.server.data.auth.AccountInfo;
import com.lamld.supportmanualtest.server.data.pojo.OpenApiDefinition.DataSchema;
import com.lamld.supportmanualtest.server.data.pojo.OpenApiDefinition.Method;
import com.lamld.supportmanualtest.server.data.pojo.OpenApiDefinition.OpenApiDefinition;
import com.lamld.supportmanualtest.server.data.pojo.OpenApiDefinition.Parameter;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
@Log4j2
public class ImportService extends BaseService {
  public ImportResponse importOpenApiDoc(AccountInfo accountInfo, OpenApiDefinition document) {
    ImportResponse importResponse = new ImportResponse();
    importResponse.setProjectName(document.getInfo().getTitle());
    importResponse.setUrl(document.getServers().stream().findFirst().orElse(new OpenApiDefinition.Server()).getUrl());

    List<ApiInfo> apiInfos = new ArrayList<>();

    // Lặp qua tất cả các đường dẫn trong OpenApiDefinition
    for (Map.Entry<String, OpenApiDefinition.Path> entry : document.getPaths().entrySet()) {
      String path = entry.getKey();
      OpenApiDefinition.Path apiPath = entry.getValue();

      // Lấy thông tin cho từng phương thức
      addApiInfo(apiInfos, path, apiPath.getGet());
      addApiInfo(apiInfos, path, apiPath.getPost());
      addApiInfo(apiInfos, path, apiPath.getPut());
      addApiInfo(apiInfos, path, apiPath.getDelete());
    }
    setBodyToApiInfo(apiInfos, document.getComponents());
    importResponse.setApiInfos(apiInfos); // Đặt danh sách thông tin API vào phản hồi
    return importResponse;
  }

  private void addApiInfo(List<ApiInfo> apiInfos, String path, Method method) {
    if (method != null) {
      ApiInfo apiInfo = new ApiInfo();
      apiInfo.setRouter(path);
      for (Parameter parameter : method.getParameters()) {
        if (Objects.equals(parameter.getIn(), "query")) {
          apiInfo.getParameters().add(parameter.getName());
        } else {
          apiInfo.getPathVariables().add(parameter.getName());
        }
      }

      if (method.getRequestBody() != null
          && method.getRequestBody().getContent() != null
          && method.getRequestBody().getContent().getApplicationJson() != null
          && method.getRequestBody().getContent().getApplicationJson().getSchema() != null) {
        apiInfo.setMappingBodyName(method.getRequestBody().getContent().getApplicationJson().getSchema().getRef());
      }

      apiInfos.add(apiInfo);
    }
  }

  public void setBodyToApiInfo(List<ApiInfo> apiInfos, OpenApiDefinition.Components components) {
    for (ApiInfo apiInfo : apiInfos) {
      if (apiInfo.getMappingBodyName() != null) {
        String ref = apiInfo.getMappingBodyName().substring(apiInfo.getMappingBodyName().lastIndexOf("/") + 1);
        Map<String, OpenApiDefinition.ComponentSchema> schema = components.getSchemas();
        List<ApiInfo.FieldValidate> fieldValidates = new ArrayList<>();

        // Kiểm tra các schema trong components
        for (Map.Entry<String, OpenApiDefinition.ComponentSchema> entry : schema.entrySet()) {
          if (entry.getKey().equals(ref)) {
            // Duyệt qua các thuộc tính trong schema
            for (Map.Entry<String, DataSchema> propertiesEntry : entry.getValue().getProperties().entrySet()) {
              ApiInfo.FieldValidate fieldValidate = new ApiInfo.FieldValidate(propertiesEntry.getKey(), propertiesEntry.getValue());

              // Gọi hàm đệ quy để tìm các trường con nếu có
              addChildFields(fieldValidate, components, new HashSet<>());
              fieldValidates.add(fieldValidate);
            }
          }
        }
        apiInfo.setBody(fieldValidates);
      }
    }
  }

  // Hàm đệ quy để thêm các trường con vào FieldValidate
  private void addChildFields(ApiInfo.FieldValidate parentField, OpenApiDefinition.Components components, Set<String> processedRefs) {
    if (parentField.getRef() != null) {
      String childRef = parentField.getRef().substring(parentField.getRef().lastIndexOf("/") + 1);

      // Kiểm tra xem ref đã được xử lý chưa
      if (processedRefs.contains(childRef)) {
        return; // Nếu đã xử lý, không làm gì nữa
      }

      // Thêm ref vào tập hợp đã xử lý
      processedRefs.add(childRef);

      OpenApiDefinition.ComponentSchema childSchema = components.getSchemas().get(childRef);

      if (childSchema != null) {
        // Duyệt qua tất cả các thuộc tính của childSchema
        for (Map.Entry<String, DataSchema> childEntry : childSchema.getProperties().entrySet()) {
          ApiInfo.FieldValidate childFieldValidate = new ApiInfo.FieldValidate(childEntry.getKey(), childEntry.getValue());

          // Gọi lại hàm để kiểm tra nếu childFieldValidate cũng có ref
          addChildFields(childFieldValidate, components, processedRefs); // Sử dụng processedRefs hiện tại
          // Khởi tạo danh sách body nếu chưa có
          if (parentField.getBody() == null) {
            parentField.setBody(new ArrayList<>());
          }
          // Thêm child vào danh sách body
          parentField.getBody().add(childFieldValidate);
        }
      }
    }
  }

}
