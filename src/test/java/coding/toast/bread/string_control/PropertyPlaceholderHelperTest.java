package coding.toast.bread.string_control;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.util.PropertyPlaceholderHelper;

import java.util.Map;
import java.util.Properties;

import static java.util.Map.entry;
import static org.assertj.core.api.Assertions.assertThat;


/**
 * Testing Spring's PropertyPlaceholderHelper class
 */
public class PropertyPlaceholderHelperTest {
	
	@Test
	@DisplayName("Create complex postgre ddl sql")
	void test1() {
		
		PropertyPlaceholderHelper placeholder = new PropertyPlaceholderHelper("${", "}");
		Properties properties = new Properties();
		properties.putAll(
			Map.ofEntries(
				entry("schema", "coding_toast"),
				entry("table_name", "customer"),
				entry("id_column_name", "id"),
				entry("column_1_name", "name"),
				entry("colum_1_type", "varchar(50)"),
				entry("column_2_name", "phone_number"),
				entry("colum_2_type", "varchar(20)"),
				entry("column_3_name", "sex"),
				entry("colum_3_type", "boolean"),
				entry("column_4_name", "region"),
				entry("colum_4_type", "varchar(40)")
			)
		);
		
		String ddlString = placeholder.replacePlaceholders(
			"""
				create table ${schema}.${table_name} (
					"${id_column_name}" serial not null,
					"${column_1_name}" ${colum_1_type},
					"${column_2_name}" ${colum_2_type},
					"${column_3_name}" ${colum_3_type},
					"${column_4_name}" ${colum_4_type},
					constraint "pk_for_${schema}_${table_name}" primary key (${id_column_name})
				)""", properties);
		
		
		assertThat(ddlString).isEqualTo(
    """
			create table coding_toast.customer (
				"id" serial not null,
				"name" varchar(50),
				"phone_number" varchar(20),
				"sex" boolean,
				"region" varchar(40),
				constraint "pk_for_coding_toast_customer" primary key (id)
			)""");
		
		
	}
	
	@Test
	@DisplayName("Create complex postgre ddl sql using PlaceholderResolver")
	void test2() {
		
		PropertyPlaceholderHelper placeholder = new PropertyPlaceholderHelper("${", "}");
		
		String ddlString = placeholder.replacePlaceholders(
			"""
				create table ${schema}.${table_name} (
					"${id_column_name}" serial not null,
					"${column_1_name}" ${colum_1_type},
					"${column_2_name}" ${colum_2_type},
					"${column_3_name}" ${colum_3_type},
					"${column_4_name}" ${colum_4_type},
					constraint "pk_for_${schema}_${table_name}" primary key (${id_column_name})
				)""", placeholderName
				-> switch (placeholderName) {
				case "schema" -> "coding_toast";
				case "table_name" -> "customer";
				case "id_column_name" -> "id";
				case "column_1_name" -> "name";
				case "colum_1_type" -> "varchar(50)";
				case "column_2_name" -> "phone_number";
				case "colum_2_type" -> "varchar(20)";
				case "column_3_name" -> "sex";
				case "colum_3_type" -> "boolean";
				case "column_4_name" -> "region";
				case "colum_4_type" -> "varchar(40)";
				default -> throw new IllegalStateException("Unexpected value: " + placeholderName);
			});
		
		assertThat(ddlString).isEqualTo(
	"""
			create table coding_toast.customer (
				"id" serial not null,
				"name" varchar(50),
				"phone_number" varchar(20),
				"sex" boolean,
				"region" varchar(40),
				constraint "pk_for_coding_toast_customer" primary key (id)
			)""");
	}
	
}
