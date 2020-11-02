package net.hka.common.web.rest.api.assembler;

/**
 * Interface for components that convert a DTO entity type into a domain type.
 *
 * @author Hany Kamal
 */
public interface ApiEntityAssembler<T, D> { // D extends RepresentationModel<D>
	/**
	 * Converts the {@code D} into a {@code T}.
	 *
	 * @param data transfer object (DTO) 
	 * @return
	 */
	T toEntity(final D dto);
}
